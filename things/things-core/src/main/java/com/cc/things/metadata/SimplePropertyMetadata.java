package com.cc.things.metadata;

import com.cc.things.metadata.types.DataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class SimplePropertyMetadata implements PropertyMetadata {

    private DataType valueType;

    private String id;

    private String name;

    private String description;

    private Map<String, Object> expands;

    public static SimplePropertyMetadata of(String id, String name, DataType type) {
        SimplePropertyMetadata metadata = new SimplePropertyMetadata();
        metadata.setId(id);
        metadata.setName(name);
        metadata.setValueType(type);
        return metadata;
    }

    @Override
    public PropertyMetadata merge(PropertyMetadata another, MergeOption... option) {
        SimplePropertyMetadata metadata = this.copy();
        if (metadata.expands == null) {
            metadata.expands = new HashMap<>();
        }
        if (MapUtils.isNotEmpty(another.getExpands())) {
            another.getExpands().forEach(metadata.expands::put);
        }
        return metadata;
    }

    private SimplePropertyMetadata copy() {
        SimpleDeviceMetadata metadata = new SimpleDeviceMetadata();
        metadata.setId(this.id);
        metadata.setName(this.name);
        metadata.setDescription(this.description);
        metadata.setExpands(this.expands);
        return null;
    }
}
