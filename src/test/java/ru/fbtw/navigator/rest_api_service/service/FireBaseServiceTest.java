package ru.fbtw.navigator.rest_api_service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.fbtw.navigator.rest_api_service.math.Edge;
import ru.fbtw.navigator.rest_api_service.math.GraphNode;
import ru.fbtw.navigator.rest_api_service.navigation.Level;
import ru.fbtw.navigator.rest_api_service.navigation.Node;
import ru.fbtw.navigator.rest_api_service.navigation.NodeType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FireBaseServiceTest {

    @Autowired
    FireBaseService service;

    Set<Level> levels;
    Set<Node> nodes;
    Set<Edge> edges;
    private String test_user;

    @BeforeEach
    void setUp() {
        levels = new HashSet<Level>(){{
           add(new Level("lvl1","sdfsfdsf="));
           add(new Level("lvl2","sdfsfdsf="));
           add(new Level("lvl3","sdfsfdsf="));
           add(new Level("lvl4","sdfsfdsf="));
           add(new Level("lvl5","sdfsfdsf="));
           add(new Level("lvl6","sdfsfdsf="));
           add(new Level("lvl6","sdfsfdsf="));
        }};

        Level parent = levels.iterator().next();

        nodes= new HashSet<Node>(){{
           add(new Node(12,12,",","node0",parent, NodeType.TEMP,false));
           add(new Node(12,12,",","node2",parent, NodeType.TEMP,false));
           add(new Node(12,12,",","node3",parent, NodeType.TEMP,false));
           add(new Node(12,12,",","node4",parent, NodeType.TEMP,false));
           add(new Node(12,12,",","node5",parent, NodeType.TEMP,false));
        }};

        Iterator<Node> iterator = nodes.iterator();

        GraphNode a = new GraphNode(iterator.next());
        GraphNode b = new GraphNode(iterator.next());

        edges = new HashSet<Edge>(){{
           add(new Edge(a,b));
        }};

        test_user = "test_user";

      // service = new FireBaseService("mapper-c0c6a");
    }

    @Test
    void createUser() {
        try {
            service.createUser(test_user,levels,nodes,edges);
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail();
        }

    }

    @Test
    void updateUser() {

        service.updateUser(test_user,levels,nodes,edges);
    }

    @Test
    void remove(){
        service.remove(test_user);
    }
}