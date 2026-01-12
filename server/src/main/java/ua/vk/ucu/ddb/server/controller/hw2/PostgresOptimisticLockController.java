package ua.vk.ucu.ddb.server.controller.hw2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.vk.ucu.ddb.server.dto.UserCounter;

@Slf4j
@RestController
@RequestMapping("/counter/postgres/optimistic-lock")
public class PostgresOptimisticLockController extends BasePostgresController {

    public PostgresOptimisticLockController(DataSource dataSource) {
        super(dataSource);
    }

    @PostMapping
    public void increment() throws SQLException {
        while (true) {
            try (Connection conn = dataSource.getConnection()) {
                conn.setAutoCommit(false);

                UserCounter counter = getCurrentCounter(conn);

                try (PreparedStatement update = conn.prepareStatement(
                    "UPDATE user_counter SET counter = ?, version = ? WHERE user_id = ? AND version = ?")) {

                    update.setLong(1, counter.counter() + 1);
                    update.setInt(2, counter.version() + 1);
                    update.setLong(3, counter.userId());
                    update.setInt(4, counter.version());

                    int rowsAffected = update.executeUpdate();
                    if (rowsAffected > 0) {
                        conn.commit();
                        break;
                    }
                }
            }
        }
        
    }

    @SneakyThrows
    private UserCounter getCurrentCounter(Connection conn) {
        try (PreparedStatement select = conn.prepareStatement(
                    "SELECT user_id, counter, version FROM user_counter WHERE user_id = 1")) {
            ResultSet rs = select.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Row for user_id=1 not found");
            }

            return new UserCounter(
                rs.getLong("user_id"), 
                rs.getLong("counter"), 
                rs.getInt("version")
            );
        } 
    }

}
