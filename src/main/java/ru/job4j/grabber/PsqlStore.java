package ru.job4j.grabber;

import ru.job4j.grabber.utils.Store;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection connection;

    public PsqlStore(Properties config) {
        try {
            Class.forName(config.getProperty("jdbc.driver"));
            String url = config.getProperty("jdbc.url");
            String login = config.getProperty("jdbc.username");
            String password = config.getProperty("jdbc.password");
            this.connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Post createPost() {
        return new Post();
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO post(title, link, description, created) "
                        + "VALUES (?, ?, ?, ?) "
                        + "ON CONFLICT (link)"
                        + "DO NOTHING",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet generateKeys = statement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    post.setId(generateKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM post;")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = createPost();
                    post.setId(resultSet.getInt("id"));
                    post.setTitle(resultSet.getString("title"));
                    post.setDescription(resultSet.getString("description"));
                    post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = createPost();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM post WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                post.setId(resultSet.getInt("id"));
                post.setTitle(resultSet.getString("title"));
                post.setDescription(resultSet.getString("description"));
                post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
