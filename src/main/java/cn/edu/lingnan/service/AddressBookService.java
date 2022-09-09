package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface AddressBookService extends IService<AddressBook> {
    R<String> setDefaultAddress(Map map);
}
