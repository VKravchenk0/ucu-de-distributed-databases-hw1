package ua.vk.ucu.ddb.server.controller.hw2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@RequiredArgsConstructor
public abstract class BasePostgresController {

    protected final DataSource dataSource;

    @GetMapping
    public long count() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return getCurrentCounterValue(conn);
        }
    }

    @DeleteMapping
    public void reset() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE user_counter SET counter = 0, version = 0 WHERE user_id = 1")) {

            ps.executeUpdate();
        }
    }

    protected long getCurrentCounterValue(Connection conn) throws SQLException {
        long counter;

        try (PreparedStatement select = conn.prepareStatement(
                "SELECT counter FROM user_counter WHERE user_id = 1")) {

            ResultSet rs = select.executeQuery();
            rs.next();
            counter = rs.getLong(1);
        }
        return counter;
    }

}
