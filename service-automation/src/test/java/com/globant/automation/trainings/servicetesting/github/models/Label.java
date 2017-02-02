package com.globant.automation.trainings.servicetesting.github.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * ENTITY RELATED INFORMATION IS MADE UP! DO NOT ASSUME IT IS RELATED TO GITHUB!
 * <p>
 * No getters/setters for brevity
 *
 * @author Juan Krzemien
 */
@Entity
@Table(name = "LABELS")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    @JsonIgnore
    public Long id;

    @Column(name = "URL")
    @JsonProperty
    public String url;

    @Column(name = "NAME")
    @JsonProperty
    public String name;

    @Column(name = "COLOR")
    @JsonProperty
    public String color;

}
