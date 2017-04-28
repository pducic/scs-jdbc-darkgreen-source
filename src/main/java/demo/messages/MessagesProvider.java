package demo.messages;

import demo.database.ConnectionProvider;
import demo.database.TrafficDatabaseProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class MessagesProvider {

    @Autowired
    private ConnectionProvider connectionProvider;
    @Autowired
    private TrafficDatabaseProvider trafficDatabaseProvider;

    private final String procedure;
    private final int greenId;
    private final int maxPageSize;
    private final String columns;
    private final boolean uppercaseColumnNames;

    public MessagesProvider(String columns, String procedure, int greenId, int maxPageSize, boolean uppercaseColumnNames) {
        this.columns = columns;
        this.procedure = procedure;
        this.greenId = greenId;
        this.maxPageSize = maxPageSize;
        this.uppercaseColumnNames = uppercaseColumnNames;
    }

    public Collection<Map<String, Object>> getMessages() {
        Set<Integer> activeDatabaseIds = trafficDatabaseProvider.getActiveDatabaseIds();
        List<DarkGreenReader> darkGreenReaders = activeDatabaseIds.stream()
                .map(integer -> new DarkGreenReader(connectionProvider, procedure, greenId, integer, maxPageSize, columns, uppercaseColumnNames))
                .collect(Collectors.toList());

        Collection<Map<String, Object>> messages = new ArrayList<>();
        darkGreenReaders.forEach(darkGreenReader -> {
            Collection<Map<String, Object>> darkGreenReaderMessages = darkGreenReader.getMessages();
            messages.addAll(darkGreenReaderMessages);
        });
        return messages;
    }
}
