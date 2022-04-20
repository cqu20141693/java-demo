package com.wujt.influx.domin;

import com.wujt.influx.domin.schema.ServerInfoSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.annotation.TimeColumn;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Measurement(name = ServerInfoSchema.MEASUREMENT)
public class ServerCountInfo {
    /**
     * 时间戳
     */
    @TimeColumn(timeUnit = TimeUnit.MILLISECONDS)
    private Instant time;
    @Column(
            name = "count_name"
    )
    private long nameCount;

    @Column(
            name = "count_current_process"
    )
    private long processCount;

    public long getCount() {
        return nameCount > processCount ? nameCount : processCount;
    }

}
