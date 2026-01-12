package ua.vk.ucu.ddb.server.controller.hw2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@RestController
@RequestMapping("/counter/postgres/in-place-update")
public class PostgresInplaceUpdateController extends BasePostgresController {

    public PostgresInplaceUpdateController(DataSource dataSource) {
        super(dataSource);
    }

    @PostMapping
    public void increment() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement update = conn.prepareStatement(
                "UPDATE user_counter SET counter = counter + 1 WHERE user_id = ?")) {

            update.setLong(1, 1);
            update.executeUpdate();
        }

            conn.commit();
        }
    }

}
