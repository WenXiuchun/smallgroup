package com.cc.smallgroup.entity;

public class User {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotNull
    @Column(name = "userName")
    private String userName;
    @Column(name = "password")
    private String password;

    @Column(name = "alias")
    private String alias;
    @Column(name = "introduceOne")
    private String introduceOne;
    @Column(name = "introduceTwo")
    private String introduceTwo;
    @Column(name = "pic")
    private String pic;
    @Column(name = "career")
    private String career;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "age")
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIntroduceOne() {
        return introduceOne;
    }

    public void setIntroduceOne(String introduceOne) {
        this.introduceOne = introduceOne;
    }

    public String getIntroduceTwo() {
        return introduceTwo;
    }

    public void setIntroduceTwo(String introduceTwo) {
        this.introduceTwo = introduceTwo;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
