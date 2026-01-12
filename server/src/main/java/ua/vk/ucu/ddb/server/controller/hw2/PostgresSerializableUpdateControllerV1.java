package ua.vk.ucu.ddb.server.controller.hw2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@RestController
@RequestMapping("/counter/postgres/serializable-update-v1")
public class PostgresSerializableUpdateControllerV1 extends BasePostgresController {

    public PostgresSerializableUpdateControllerV1(DataSource dataSource) {
        super(dataSource);
    }

    @PostMapping
    public void increment() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

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
        } catch (Exception e) {
            // не логуємо помилки
        }
    }

}
