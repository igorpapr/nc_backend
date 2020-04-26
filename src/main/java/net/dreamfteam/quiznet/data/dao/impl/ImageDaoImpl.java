package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class ImageDaoImpl implements ImageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ImageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String saveImage(MultipartFile image) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        byte [] byteArr = {};
        try {
            byteArr = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] finalByteArr = byteArr;
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO images (image) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                ps.setBytes(1, finalByteArr);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKeys().get("image_id").toString();
    }

    @Override
    public byte[] loadImage(String imageId) {
        byte[] image = jdbcTemplate.queryForObject(
                "SELECT image FROM images WHERE image_id = UUID(?)",
                new Object[]{imageId}, (rs, rowNum) -> rs.getBytes(1));
        return image;
    }

    @Override
    public void deleteImage(String imageId) {
        jdbcTemplate.update("DELETE FROM images WHERE image_id = UUID(?)", imageId);
        System.out.println("Image deleted. Id: " + imageId);
    }
}
