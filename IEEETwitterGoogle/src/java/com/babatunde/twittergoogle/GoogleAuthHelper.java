package com.babatunde.twittergoogle;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * A helper class for Google's OAuth2 authentication API.
 *
 * @version 20130224
 * @author Matyas Danter
 */
public final class GoogleAuthHelper {

    /**
     * Please provide a value for the CLIENT_ID constant before proceeding, set
     * this up at https://code.google.com/apis/console/
     */
    private static final String CLIENT_ID = "";
    /**
     * Please provide a value for the CLIENT_SECRET constant before proceeding,
     * set this up at https://code.google.com/apis/console/
     */
    private static final String CLIENT_SECRET = "";

    /**
     * Callback URI that google will redirect to after successful authentication
     */
    private static final String CALLBACK_URI = "";// Come
    // back
    // to
    // fix
    // this...

    // start google authentication constants
    private static final List<String> SCOPE = Arrays.asList(
            "https://www.googleapis.com/auth/plus.me",
            "https://www.googleapis.com/auth/plus.stream.write",
            "https://www.googleapis.com/auth/plus.stream.read",
            "https://www.googleapis.com/auth/plus.circles.read",
            "https://www.googleapis.com/auth/plus.circles.write");
    private static final String USER_INFO_URL
            = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    // end google authentication constants

    private GoogleTokenResponse response;
    private String stateToken;

    private final GoogleAuthorizationCodeFlow flow;

    /**
     * Constructor initializes the Google Authorization Code Flow with CLIENT
     * ID, SECRET, and SCOPE
     */
    public GoogleAuthHelper() {
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE)
                .setApprovalPrompt("force").setAccessType("offline").build();

        generateStateToken();
    }

    /**
     * Builds a login URL based on client ID, secret, callback URI, and scope
     */
    public String buildLoginUrl() {

        final GoogleAuthorizationCodeRequestUrl url = flow
                .newAuthorizationUrl();

        return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
    }

    /**
     * Generates a secure state token
     */
    private void generateStateToken() {

        SecureRandom sr1 = new SecureRandom();

        stateToken = "google;" + sr1.nextInt();

    }

    /**
     * Accessor for state token
     */
    public String getStateToken() {
        return stateToken;
    }

    /**
     * Expects an Authentication Code, and makes an authenticated request for
     * the user's profile information
     *
     * @return JSON formatted user profile information
     * @param authCode authentication code provided by google
     */
    public PlusDomains getPlusDomain(GoogleTokenResponse responded)
            throws IOException {

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .addRefreshListener(new CredentialRefreshListener() {
                    @Override
                    public void onTokenErrorResponse(Credential credential,
                            TokenErrorResponse tokenErrorResponse)
                    throws IOException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onTokenResponse(Credential credential,
                            TokenResponse tokenResponse) throws IOException {
                        // TODO Auto-generated method stub
                    }
                })
                // You can also add a credential store listener to have
                // credentials
                // stored automatically.
                // .addRefreshListener(new
                // CredentialStoreRefreshListener(userId, credentialStore))
                .build();
        credential.setFromTokenResponse(responded);
        // Though not necessary when first created, you can manually refresh the
        // token, which is needed after 60 minutes.
        credential.refreshToken();

        // Create and return the authorized API client
        PlusDomains service = new PlusDomains.Builder(HTTP_TRANSPORT,
                JSON_FACTORY, credential).build();
        return service;

    }

    public GoogleTokenResponse getResponse(String code) throws IOException {
        response = flow.newTokenRequest(code)
                .setRedirectUri(CALLBACK_URI).execute();

        return response;
    }

}
