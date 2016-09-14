package com.globant.automation.trainings.servicetesting.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Juan Krzemien
 */
public class User {

    @JsonProperty("login")
    public String login;

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("avatar_url")
    public String avatarUrl;

    @JsonProperty("gravatar_id")
    public String gravatarId;

    @JsonProperty("url")
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

    @JsonProperty("type")
    public String type;

    @JsonProperty("site_admin")
    public Boolean siteAdmin;

    @JsonProperty("name")
    public String name;

    @JsonProperty("company")
    public String company;

    @JsonProperty("blog")
    public String blog;

    @JsonProperty("location")
    public String location;

    @JsonProperty("email")
    public String email;

    @JsonProperty("hireable")
    public Boolean hireable;

    @JsonProperty("bio")
    public String bio;

    @JsonProperty("public_repos")
    public Integer publicRepos;

    @JsonProperty("public_gists")
    public Integer publicGists;

    @JsonProperty("followers")
    public Integer followers;

    @JsonProperty("following")
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

    @JsonProperty("collaborators")
    public Integer collaborators;

    @JsonProperty("plan")
    public Plan plan;

}



