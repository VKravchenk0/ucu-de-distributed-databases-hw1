package ua.vk.ucu.ddb.hw1.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@RestController
@RequestMapping("/counter/postgres/lost-update")
public class PostgresLostUpdateController extends BasePostgresController {

    public PostgresLostUpdateController(DataSource dataSource) {
        super(dataSource);
    }

    @PostMapping
    public void increment() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            long counter = getCurrentCounterValue(conn);

            updateCounterValue(conn, counter);

            conn.commit();
        }
    }

    private void updateCounterValue(Connection conn, long counter) throws SQLException {
        counter = counter + 1;

        try (PreparedStatement update = conn.prepareStatement(
                "UPDATE user_counter SET counter = ? WHERE user_id = ?")) {

            update.setLong(1, counter);
            update.setLong(2, 1);
            update.executeUpdate();
        }
    }

}
