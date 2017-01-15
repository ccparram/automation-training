package com.globant.automation.trainings.servicetesting.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * No getters/setters for brevity
 *
 * @author Juan Krzemien
 */
public class User {

    @JsonProperty
    public String login;

    @JsonProperty
    public Integer id;

    @JsonProperty("avatar_url")
    public String avatarUrl;

    @JsonProperty("gravatar_id")
    public String gravatarId;

    @JsonProperty
    public String url;

    @JsonProperty("html_url")
    public String htmlUrl;

    @JsonProperty("followers_url")
    public String followersUrl;

    @JsonProperty("following_url")
    public String followingUrl;

    @JsonProperty("gists_url")
    public String gistsUrl;

    @JsonProperty("starred_url")
    public String starredUrl;

    @JsonProperty("subscriptions_url")
    public String subscriptionsUrl;

    @JsonProperty("organizations_url")
    public String organizationsUrl;

    @JsonProperty("repos_url")
    public String reposUrl;

    @JsonProperty("events_url")
    public String eventsUrl;

    @JsonProperty("received_events_url")
    public String receivedEventsUrl;

    @JsonProperty
    public String type;

    @JsonProperty("site_admin")
    public Boolean siteAdmin;

    @JsonProperty
    public String name;

    @JsonProperty
    public String company;

    @JsonProperty
    public String blog;

    @JsonProperty
    public String location;

    @JsonProperty
    public String email;

    @JsonProperty
    public Boolean hireable;

    @JsonProperty
    public String bio;

    @JsonProperty("public_repos")
    public Integer publicRepos;

    @JsonProperty("public_gists")
    public Integer publicGists;

    @JsonProperty
    public Integer followers;

    @JsonProperty
    public Integer following;

    @JsonProperty("created_at")
    public String createdAt;

    @JsonProperty("updated_at")
    public String updatedAt;

    @JsonProperty("total_private_repos")
    public Integer totalPrivateRepos;

    @JsonProperty("owned_private_repos")
    public Integer ownedPrivateRepos;

    @JsonProperty("private_gists")
    public Integer privateGists;

    @JsonProperty("disk_usage")
    public Integer diskUsage;

    @JsonProperty
    public Integer collaborators;

    @JsonProperty
    public Plan plan;

}



