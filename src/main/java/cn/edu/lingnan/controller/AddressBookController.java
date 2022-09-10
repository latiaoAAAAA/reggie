package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.AddressBook;
import cn.edu.lingnan.service.AddressBookService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 获取默认地址
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> defaultAddress(){
        AddressBook userAddress = addressBookService.getOne(
                new LambdaQueryWrapper<AddressBook>()
                        .eq(AddressBook::getUserId, ThreadLocalUtil.get())
                        .eq(AddressBook::getIsDefault,1)
        );
        log.info("获取默认地址:{}",userAddress.toString());
        return userAddress!=null?R.success(userAddress):R.error("获取默认地址失败！");
    }

    /**
     * 获取所有地址
     * @return
     */
    @GetMapping("/list")
    public R<List> listAddress(){
        List<AddressBook> addressBooks = addressBookService.list(
                new LambdaQueryWrapper<AddressBook>().eq(AddressBook::getUserId, ThreadLocalUtil.get())
        );
        log.info("获取所有地址:{}",addressBooks.toString());
        return (addressBooks==null||addressBooks.isEmpty())?R.error("获取地址失败！"):R.success(addressBooks);
    }

    /**
     * 获取单个收货地址信息---修改时回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> getOne(@PathVariable("id") Long id){
        AddressBook address = addressBookService.getById(id);
        log.info("获取单个地址:{}",address.toString());
        return address==null?R.error("获取收货地址信息失败！"):R.success(address);
    }

    /**
     * 修改收货地址信息
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook){
        boolean isSuccess = addressBookService.updateById(addressBook);
        log.info("修改地址:{}",addressBook.toString());
        return isSuccess?R.success("修改成功！"):R.error("修改失败！");
    }

    /**
     * 添加收货地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> addAddress(@RequestBody AddressBook addressBook){
        addressBook.setUserId(ThreadLocalUtil.get());
        boolean isSuccess = addressBookService.save(addressBook);
        log.info("添加地址:{}",addressBook.toString());
        return isSuccess?R.success("添加收货地址成功！"):R.error("添加收货地址失败！");
    }

    /**
     * 设置默认地址
     * @param map
     * @return
     */
    @PutMapping("/default")
    public R<String> setDefaultAddress(@RequestBody Map map){
        return addressBookService.setDefaultAddress(map);
    }

    /**
     * 删除收货地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteAddress(@RequestParam("ids") List<Long> ids){
        boolean isSuccess = addressBookService.removeByIds(ids);
        log.info("删除地址,Ids:{}",ids.toString());
        return isSuccess?R.success("删除收货地址成功"):R.error("删除收货地址失败");
    }
}
