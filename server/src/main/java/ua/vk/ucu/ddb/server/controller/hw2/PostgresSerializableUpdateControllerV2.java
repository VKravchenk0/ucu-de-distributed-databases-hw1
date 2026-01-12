package ua.vk.ucu.ddb.server.controller.hw2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@RestController
@RequestMapping("/counter/postgres/serializable-update-v2")
public class PostgresSerializableUpdateControllerV2 extends BasePostgresController {

    public PostgresSerializableUpdateControllerV2(DataSource dataSource) {
        super(dataSource);
    }

    @PostMapping
    public void increment() throws SQLException {
        while (true) {
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement update = conn.prepareStatement(
                "UPDATE user_counter SET counter = ? WHERE user_id = ?")
                ) {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                long counter = getCurrentCounterValue(conn);
                counter = counter + 1;

                update.setLong(1, counter);
                update.setLong(2, 1);
                try {
                    int rowsAffected = update.executeUpdate();
                    if (rowsAffected > 0) {
                        conn.commit();
                        break;
                    }
                } catch (SQLException e) {
                    if (isSerializationFailure(e)) {
                        // нічого не робимо, йдемо на наступну ітерацію
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    private boolean isSerializationFailure(SQLException e) {
        return "40001".equals(e.getSQLState());
    }

}
