package cn.reebook.registration;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

/**
 * Created by lirui on 2017/4/21.
 */
public class Registration {

    private String id;
    @Email(message = "邮箱格式错误")
    private String email;
    @NotNull(message = "密码不可以为空")
    private String password;
    @NotNull(message = "用户名不可以为空")
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
