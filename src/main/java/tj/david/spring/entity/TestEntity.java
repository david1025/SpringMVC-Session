package tj.david.spring.entity;

import java.io.Serializable;

/**
 * 实体类必须实现 Serializable 序列化接口，否则无法存到redis中
 * Created by David on 2016/7/22.
 */
public class TestEntity implements Serializable {
    private static final long serialVersionUID = 1602875866651176603L;

    private int id;
    private String username;
    private String trueName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
