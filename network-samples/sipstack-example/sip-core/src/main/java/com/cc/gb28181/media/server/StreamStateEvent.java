package com.cc.gb28181.media.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class StreamStateEvent {

    public enum Type{
        CREATED,
        PLAY,
        STOP_PLAY,
        NO_READER,
        CLOSED,
        RECORD_FILE
    }

    private Type type;
    private String mediaServerId;
    private String streamId;
    private Map<String, Object> others;
}
