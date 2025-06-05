package cn.edu.xaut.quanyou.Mapper;

import cn.edu.xaut.quanyou.Model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
* @author 86147
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-05-16 19:05:13
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




