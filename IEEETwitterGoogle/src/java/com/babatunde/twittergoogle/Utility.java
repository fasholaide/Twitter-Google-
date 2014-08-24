package com.babatunde.twittergoogle;

import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.PlusDomains.People;
import com.google.api.services.plusDomains.model.Acl;
import com.google.api.services.plusDomains.model.Activity;
import com.google.api.services.plusDomains.model.PeopleFeed;
import com.google.api.services.plusDomains.model.Person;

import com.google.api.services.plusDomains.model.PlusDomainsAclentryResource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class Utility {

    Configuration configuration;
    StatusListener listener;
    int numberofActivities = 0;
    Queue<String> emails = new LinkedList<>();

    public Utility() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("coTcSO8Wql7rbXh7CNSNNy2mX");
        builder.setOAuthConsumerSecret("fkyGIiQb7tSZMgAChtsMtNC4GoP3wLXpRqPThfc9qbRO0E42G5");
        builder.setOAuthAccessToken("202193994-igJ1f1yfFDf0Nw0W5tgaLBoxVvbLQTlMVbXOmgsf");
        builder.setOAuthAccessTokenSecret("BWnjdn5bz2XyoHkwSulJAQkfiw6BcANXWsyKDl1AD0oR7");
        configuration = builder.build();
    }

    public void postToGoogle(PlusDomains s, String id, String hashtag) {
        try {
            final PlusDomains serve = s;
            final String circleID = id;
            listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    String msg = status.getUser().getName() + " - "
                            + "@" + status.getUser().getScreenName()
                            + " - " + status.getText();
                    System.out.println(msg);
                    //Create a list of ACL entries
                    if (serve != null && (!circleID.isEmpty() || (circleID != null))) {
                        PlusDomainsAclentryResource resource = new PlusDomainsAclentryResource();
                        resource.setType("domain").setType("circle").setId(circleID);

                        //Get Emails of people in the circle.
                        List<PlusDomainsAclentryResource> aclEntries = new ArrayList<PlusDomainsAclentryResource>();
                        aclEntries.add(resource);

                        Acl acl = new Acl();

                        acl.setItems(aclEntries);
                        acl.setDomainRestricted(true); // Required, this does the domain restriction

                        // Create a new activity object
                        Activity activity = new Activity().setObject(
                                new Activity.PlusDomainsObject()
                                .setOriginalContent(msg))
                                .setAccess(acl);
                        try {
                            // Execute the API request, which calls `activities.insert` for the logged in user
                            activity = serve.activities().insert("me", activity)
                                    .execute();
                        } catch (IOException ex) {
                            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                }

                @Override
                public void onDeletionNotice(
                        StatusDeletionNotice statusDeletionNotice
                ) {
                    System.out.println("Got a status deletion notice id:"
                            + statusDeletionNotice.getStatusId());
                }

                @Override
                public void onTrackLimitationNotice(
                        int numberOfLimitedStatuses
                ) {
                    System.out.println("Got track limitation notice:"
                            + numberOfLimitedStatuses);
                }

                @Override
                public void onScrubGeo(long userId, long upToStatusId
                ) {
                    System.out.println("Got scrub_geo event userId:"
                            + userId + " upToStatusId:" + upToStatusId);
                }

                @Override
                public void onStallWarning(StallWarning warning
                ) {
                    System.out.println("Got stall warning:" + warning);
                }

                @Override
                public void onException(Exception ex
                ) {
                    ex.printStackTrace();
                }

            };
        } catch (Exception e) {
        }

        TwitterStream twitterStream = new TwitterStreamFactory(
                configuration).getInstance();

        twitterStream.addListener(listener);
        String str[] = {hashtag};
        twitterStream.shutdown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        twitterStream.filter(
                new FilterQuery().track(str));

    }
}
