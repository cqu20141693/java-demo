package com.cc.core.repository;

import com.cc.core.index.DeviceGeo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * 利用enable 开启repository 扫面，对bean 进行代理，通过template作为client
 */
@Repository
public interface DeviceGeoRepository extends ElasticsearchRepository<DeviceGeo, String> {

    // count
    Long countByName(String name);

    // find
    List<DeviceGeo> findTopByNameAndTags(String name, String tags);

    // query
    List<DeviceGeo> queryByCreateTimeAfter(Date after);

    // query
    List<DeviceGeo> queryByName(String name);
    List<DeviceGeo> findByName(String name);
    List<DeviceGeo> getByName(String name);

    // get
    List<DeviceGeo> getByDeviceId(String deviceId);
    List<DeviceGeo> findByDeviceId(String deviceId);
    List<DeviceGeo> queryByDeviceId(String deviceId);

    // delete
    void deleteByDeviceId(String deviceId);

    //remove
    void removeByName(String name);

    //exist
    boolean existsByDeviceId(String deviceId);

    // stream
    Stream<DeviceGeo> readByName(String name);

    Streamable<DeviceGeo> streamByCreateTimeBefore(Date before);

    // GeoPage<DeviceGeo> findByLocationIsNear();
    // update 使用原生client或者是template,见service 层
}
