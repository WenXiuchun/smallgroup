package com.cc.smallgroup.entity;

public class article {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotNull
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;

    @Column(name = "createTime")
    private String createTime;
    @Column(name = "author")
    private String author;
    @Column(name = "groupId")
    private String groupId;
}
