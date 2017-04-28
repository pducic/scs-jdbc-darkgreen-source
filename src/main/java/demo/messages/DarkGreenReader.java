package demo.messages;

import demo.database.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.*;

public class DarkGreenReader {

    private final ConnectionProvider connectionProvider;

    private final String procedure;
    private final int greenId;
    private final int trafficDbId;
    private final int maxPageSize;
    private final String columns;
    private final boolean uppercaseColumnNames;

    public DarkGreenReader(ConnectionProvider connectionProvider, String procedure, int greenId, int trafficDbId, int maxPageSize, String columns, boolean uppercaseColumnNames) {
        this.connectionProvider = connectionProvider;
        this.procedure = procedure;
        this.greenId = greenId;
        this.trafficDbId = trafficDbId;
        this.maxPageSize = maxPageSize;
        this.columns = columns;
        this.uppercaseColumnNames = uppercaseColumnNames;
    }

    public Collection<Map<String, Object>> getMessages() {
        try (Connection connection = connectionProvider.getConnection()) {
            String sql = "{ call  " + getProcedure() + "( ?, ?, ?, ?, ? )}";
            try (PreparedStatement statement = connection.prepareCall(sql)) {
                statement.setInt(1, greenId);
                statement.setInt(2, trafficDbId);
                statement.setInt(3, maxPageSize);
                statement.setString(4, columns);
                statement.setString(5, "1=1");

                return executeStatement(statement);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Collection<Map<String, Object>> executeStatement(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            LinkedHashSet<String> columns = null;
            int chunkSize = 0;
            Collection<Map<String, Object>> rows = new ArrayList<>();
            while (rs.next()) {
                chunkSize++;
                if (null == columns) {
                    columns = getColumns(rs.getMetaData());
                }
                Map<String, Object> row = process(columns, rs);
                rows.add(row);
            }
            return rows;
        }
    }

    public Map<String, Object> process(LinkedHashSet<String> columns, ResultSet rs) throws SQLException {
        final HashMap<String, Object> row = new HashMap<>(columns.size());
        for (String column : columns) {
            Object objectValue = rs.getObject(column);
            if (objectValue != null) {
                row.put(column.intern(), objectValue);
            }
        }
        return row;
    }

    public String getProcedure() {
        return procedure;
    }

    private LinkedHashSet<String> getColumns(ResultSetMetaData meta) throws SQLException {
        LinkedHashSet<String> columns = new LinkedHashSet<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String name = meta.getColumnName(i);
            if (uppercaseColumnNames) {
                name = name.toUpperCase();
            }
            columns.add(name);
        }
        return columns;
    }
}
