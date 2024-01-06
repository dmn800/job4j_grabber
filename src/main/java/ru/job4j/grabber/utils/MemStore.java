package ru.job4j.grabber.utils;

import ru.job4j.grabber.Post;

import java.util.ArrayList;
import java.util.List;

public class MemStore implements Store {
    private List<Post> posts = new ArrayList<>();
    private int ids = 1;

    private int indexOf(int id) {
        int rsl = -1;
        for (int index = 0; index < posts.size(); index++) {
            if (posts.get(index).getId() == id) {
                rsl = index;
                break;
            }
        }
        return rsl;
    }

    @Override
    public void save(Post post) {
        post.setId(ids++);
        posts.add(post);
    }

    @Override
    public List<Post> getAll() {
        return List.copyOf(posts);
    }

    @Override
    public Post findById(int id) {
        int index = indexOf(id);
        return index != -1 ? posts.get(index) : null;
    }
}
