package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.AddressBook;
import cn.edu.lingnan.mapper.AddressBookMapper;
import cn.edu.lingnan.service.AddressBookService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    @Transactional
    public R<String> setDefaultAddress(Map map) {
        boolean isSuccess1 = update().set("is_default", 1).eq("id", map.get("id")).update();
        boolean isSuccess2 = update().set("is_default", 0).eq("user_id", ThreadLocalUtil.get()).notIn("id",map.get("id")).update();
        return isSuccess1&&isSuccess2?R.success("设置成功!"):R.error("设置失败");
    }
}
