package ru.fbtw.navigator.rest_api_service.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_id")
	Project project;
	String url;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "telegramserver_name")
	TelegramServer server;

	public TelegramBot(){

	}

}
