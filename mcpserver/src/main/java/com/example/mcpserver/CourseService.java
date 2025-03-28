package com.example.mcpserver;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    private List<Course> courses = new ArrayList<>();

    @Tool(name = "dv_get_courses", description = "Get a list of courses from Dan Vega")
    public List<Course> getCourses() {
        return courses;
    }
    @Tool(name = "dv_get_course", description = "Get a single course from Dan Vega by title")
    public Course getCourseByTitle(String title) {
        return courses.stream().filter(course -> course.title().equals(title)).findFirst().orElse(null);
    }

    @PostConstruct
    public void init(){
        courses.addAll(List.of(
                new Course("Building Docker from DevOps Directive", "https://www.youtube.com/watch?v=RqTEHSBrYFw&t=10450s"),
                new Course("Building MCP demo from Dan Vega", "https://www.youtube.com/watch?v=w5YVHG1j3Co&t=43s")
        ));
    }


}
