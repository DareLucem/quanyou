package cn.edu.xaut.quanyou.common;



import lombok.Data;

import java.io.Serializable;
@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 4566767654618L;
    private int pageNum;
    private int pageSize;
}
