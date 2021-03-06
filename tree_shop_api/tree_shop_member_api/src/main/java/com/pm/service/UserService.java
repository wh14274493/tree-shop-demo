package com.pm.service;

import com.pm.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/user")
public interface UserService {
    /**
     * 获取今天操作消息队列的所有消息
     * @param intefaceType 消息头标识
     * @param type produce：发送的  consume：消费的
     * @return
     */
    @GetMapping("/messages")
    Map getTodayProduceMessages(@RequestParam String intefaceType,@RequestParam String type);

    @GetMapping("/setinfo/{name}/{age}")
    Map setMemberInfo(@PathVariable String name, @PathVariable String age);

    @PostMapping("/register")
    Map register(@RequestBody User user);

    @GetMapping("/real/delete")
    Map realDeleteUserById(@RequestParam Long id);

    /**
     * 逻辑删除（修改数据库isdelete的状态）
     *
     * @param id
     * @return
     */
    @GetMapping("/virtual/delete")
    Map virtualDeleteUserById(@RequestParam Long id);

    @PostMapping("/update")
    Map update(@RequestBody User user);

    @GetMapping("/findById")
    Map findById(@RequestParam Long id);

    @GetMapping("/findAll")
    Map findAll();

}
