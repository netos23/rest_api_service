package ru.fbtw.navigator.rest_api_service.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    String name;

    @ElementCollection(targetClass = Platform.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "platforms", joinColumns = @JoinColumn(name = "project_id"))
    @Enumerated(EnumType.STRING)
    Set<Platform> platforms;

    String telegramName;
    String telegramApiKey;
    String appName;
    String userPackage;

    @Column(length = 10000000)
    String body;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
	User owner;



    public Project(){

    }

    public boolean hasPlatform(Platform platform){
        return platforms.contains(platform);
    }
}
