package my.blog.ctrl;

import lombok.extern.slf4j.Slf4j;
import my.blog.model.domain.Blog;
import my.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/blog")
public class blogCtrl {

    @Autowired
    private BlogService blogService;

    @GetMapping("/{blogId}")
    public Blog get(@PathVariable String blogId){
        log.info("Blog Get:" + blogId);
        return blogService.get(blogId);
    }


}
