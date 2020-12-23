package ru.fbtw.navigator.rest_api_service.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Slot {
	@Id
	String url;
	Integer port;

	@ManyToOne
	TelegramServer server;

	public Slot(){

	}
}
