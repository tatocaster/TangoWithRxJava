package me.tatocaster.rxjavameetup.rxbus.events;

/**
 * Created by tatocaster on 10/23/16.
 */

public class SimpleEvent extends BaseEvent {
    public String greetingText;

    public SimpleEvent(String text) {
        this.greetingText = text;
    }

}
