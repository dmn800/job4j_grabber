package ru.job4j.grabber.utils;

import ru.job4j.grabber.Post;

import java.util.List;

public interface Store extends AutoCloseable {
    void save(Post post);
    List<Post> getAll();
    Post findById(int id);
}
