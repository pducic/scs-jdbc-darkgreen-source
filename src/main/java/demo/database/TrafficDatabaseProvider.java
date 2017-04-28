package demo.database;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class TrafficDatabaseProvider {
    public Set<Integer> getActiveDatabaseIds(){
        return new HashSet<>(Arrays.asList(11, 12));
    }
}
