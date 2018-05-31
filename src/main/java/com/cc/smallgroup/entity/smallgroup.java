package com.cc.smallgroup.entity;

public class smallgroup {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotNull
    @Column(name = "groupName")
    private String groupName;
    @Column(name = "introduce")
    private String introduce;

    @Column(name = "createTime")
    private String createTime;
    @Column(name = "createBy")
    private String createBy;
}
