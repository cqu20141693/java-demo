package com.gow.codec.encode.schema;

import java.util.List;
import lombok.Data;

/**
 * @author gow
 * @date 2021/9/22
 */
@Data
public class OperationIndex {
    // 数量
    private byte[] number;
    private List<IndexLoop> indexLoops;
}
