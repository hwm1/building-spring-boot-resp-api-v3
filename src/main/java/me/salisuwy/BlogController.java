package me.salisuwy;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@RestController
@Controller
public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @GetMapping("/create")
    public String inputForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "inputform";
    }

    @GetMapping("/delete")
    public String deleteForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "delete";
    }

    @GetMapping("/")
    public String menu() {
        return "index";
    }

    @GetMapping("/blog")
    public ModelAndView index() {
        List<Blog> blog = blogRepository.findAll();
        Map<String, Object> params = new HashMap<>();
        params.put("blog", blog);
        return new ModelAndView("displayall", params);
    }

    @GetMapping("/blog/{id}")
    public ModelAndView index2(@PathVariable String id) {
        int blogId = Integer.parseInt(id);
        Blog blog = blogRepository.findById(blogId).orElse(null);
        Map<String, Object> params = new HashMap<>();
        params.put("blog", blog);
        return new ModelAndView("displayall", params);
    }

    @PostMapping("/blog/search")
    public List<Blog> search(@RequestBody Map<String, String> body) {
        String searchTerm = body.get("text");
        return blogRepository.findByTitleContainingOrContentContaining(searchTerm, searchTerm);
    }

    @PostMapping("/blog")
    public String addNewBlog(@RequestParam String title, @RequestParam String content) {
        blogRepository.save(new Blog(title, content));
        //stay on the form page
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("redirect:/create");
//        return modelAndView;
        return "added";
    }

    @PutMapping("/blog/{id}")
    public Blog update(@PathVariable String id, @RequestBody Map<String, String> body) {
        int blogId = Integer.parseInt(id);
        // getting blog
        //  Blog blog = blogRepository.findOne(blogId);
        Blog blog = blogRepository.findById(blogId).orElse(null);
        blog.setTitle(body.get("title"));
        blog.setContent(body.get("content"));
        return blogRepository.save(blog);
    }

//    @PostMapping("blog/{id}")
//    public boolean delete(@PathVariable String id) {
//        int blogId = Integer.parseInt(id);
//        //  blogRepository.delete(blogId);
//        Blog blog = blogRepository.findById(blogId).orElse(null);
//        blogRepository.delete(blog);
//        return true;
//    }
    @PostMapping("blog/{id}")
    public String delete(@PathVariable String id) {
        int blogId = Integer.parseInt(id);
        //  blogRepository.delete(blogId);
        Blog blog = blogRepository.findById(blogId).orElse(null);

        if (blog == null) {
            return "";
        }

        blogRepository.delete(blog);

//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("redirect:/delete");
//        return modelAndView;
        return "deleted";

    }

}
