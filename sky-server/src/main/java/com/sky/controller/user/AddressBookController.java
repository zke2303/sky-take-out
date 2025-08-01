package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author zhangke
 * @version 1.0
 * @description
 * @since 2025/8/1
 */

@RestController("userAddressBookController")
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端-地址簿接口")
public class AddressBookController {


    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        log.info("新增地址, addressBook={}", addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }


    /**
     * 查询登录用户所有地址
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询登录用户所有地址")
    public Result<List<AddressBook>> list() {
        log.info("查询登录用户所有地址");
        AddressBook addressBook = AddressBook.builder()
                                            .userId(BaseContext.getCurrentId())
                                            .build();

        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping(value = "/default")
    @ApiOperation(value = "查询默认地址")
    public Result<AddressBook> getDefault(){
        log.info("查询默认地址");
        AddressBook addressBook = AddressBook.builder()
                .userId(BaseContext.getCurrentId())
                .isDefault(StatusConstant.ENABLE)
                .build();

        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() > 0) {
            return Result.success(list.get(0));
        }

        return  Result.error("没有查到默认地址");

    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改地址")
    public Result update(AddressBook addressBook) {
        log.info("修改地址: {}", addressBook);

        addressBookService.update(addressBook);

        return Result.success();
    }

    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除地址")
    public Result deleteById(Long id) {
        log.info("根据id删除地址, id: {}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据id查询地址, id: {}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址, addressBook={}", addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

}
