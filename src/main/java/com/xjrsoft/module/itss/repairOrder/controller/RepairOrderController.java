package com.xjrsoft.module.itss.repairOrder.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xjrsoft.common.page.ConventPage;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.DataTransUtil;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.Func;
import com.xjrsoft.module.base.controller.XjrBaseAnnexesFileController;
import com.xjrsoft.module.base.controller.XjrBaseUserController;
import com.xjrsoft.module.base.entity.XjrBaseUser;
import com.xjrsoft.module.base.service.IXjrBaseUserService;
import com.xjrsoft.module.base.vo.AnnexesFileVo;
import com.xjrsoft.module.dingTalk.DingTalkSendMsg;
import com.xjrsoft.module.excel.controller.XjrExcelExportController;
import com.xjrsoft.module.itss.repairOrder.dto.RepairOrderDto;
import com.xjrsoft.module.itss.repairOrder.dto.RepairOrderListDto;
import com.xjrsoft.module.itss.repairOrder.dto.SaveRepairOrderFormDataDto;
import com.xjrsoft.module.itss.repairOrder.entity.RepairOrder;
import com.xjrsoft.module.itss.repairOrder.service.IRepairOrderService;
import com.xjrsoft.module.itss.repairOrder.vo.RepairOrderListVo;
import com.xjrsoft.module.itss.repairOrder.vo.RepairOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修工单表 控制器
 *
 * @author hanhe
 * @since 2022-10-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/repairorder")
@Api(value = "维修工单表", tags = "维修工单表接口")
public class RepairOrderController {

	private IXjrBaseUserService userService;
	@Autowired
	private XjrExcelExportController ExportController;

	private final IRepairOrderService repairOrderService;
	@Autowired
	private   XjrBaseAnnexesFileController xjrBaseAnnexesFileController;
	@Autowired
	private XjrBaseUserController xjrBaseUserController;

	/**
	 * 详情
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "详情", notes = "传入repairOrder")
	public Response<RepairOrderVo> getFormData(@PathVariable String id,@RequestParam( value="type", required=false) String type) {
		// 主表数据
		RepairOrder repairOrder = repairOrderService.getById(id);
		List<AnnexesFileVo> report_file;
		List<AnnexesFileVo> repair_file = null;
		if("software"==type || "software".equals(type)){
			//软件类,只有一个附件
			report_file=xjrBaseAnnexesFileController.getfileUrlByFolderId(repairOrder.getId(),"0");
		}else{
			report_file=xjrBaseAnnexesFileController.getfileUrlByFolderId(repairOrder.getId(),"0");
			repair_file=xjrBaseAnnexesFileController.getfileUrlByFolderId(repairOrder.getId(),"9");
		}
		RepairOrderVo repairOrderVo=BeanUtil.copy(repairOrder, RepairOrderVo.class);

		repairOrderVo.setReport_file(report_file);
		repairOrderVo.setRepair_file(repair_file);
		return Response.ok(repairOrderVo);
	}

	/**
	 * 自定义分页 维修工单表
	 */
	@GetMapping()
	@ApiOperation(value = "分页", notes = "传入repairOrder")
	public Response<PageOutput<RepairOrderListVo>> getPageList(@RequestParam( value="type", required=false) String type,RepairOrderListDto listDto) {
		String userId = SecureUtil.getUserId();
		listDto.setCreated_by(userId);
		listDto.setRepair_usrid(userId);
		if("software"==type || "software".equals(type)){
			//软件,所有维修人员id和名称是“software”的都是软件类
			listDto.setRepair_usrid("software");
		}
		IPage<RepairOrder> page = repairOrderService.getPageList(listDto);
		List<RepairOrderListVo> records = BeanUtil.copyList(page.getRecords(), RepairOrderListVo.class);
		// 转换列表数据
		DataTransUtil.transListShowData(records);
		System.out.println(ConventPage.getPageOutput(page.getTotal(), records));
//		ExportController.exportExcelBy("URL",ConventPage.getPageOutput(page.getTotal(), records).toString(),"测试导出");
		return Response.ok(ConventPage.getPageOutput(page.getTotal(), records));
	}

	/**
	 * 保存 维修工单表
	 */
	@PostMapping
	@ApiOperation(value = "保存", notes = "传入repairOrder")
	public Response save(@RequestBody SaveRepairOrderFormDataDto formDto,@RequestParam(value="type", required=false)String type) {
		RepairOrderDto repairOrderDto = formDto.getRepairOrderDto();
		String id=repairOrderDto.getId();
		String code;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if("software"==type || "software".equals(type)){
			//软件
			if(""==id||null==id){  //没有id是新增
				id=IdWorker.get32UUID();
				code=System.currentTimeMillis()+"";
				repairOrderDto.setId(id);
				repairOrderDto.setCode(code);
				String userId = SecureUtil.getUserId();
				repairOrderDto.setCreatedBy(userId);
				repairOrderDto.setCreatedName(SecureUtil.getUserName());
				repairOrderDto.setRepairUsrname("software");
				repairOrderDto.setRepairUsrid("software");
				repairOrderDto.setCreatedTime(LocalDateTime.now());
				RepairOrder repairOrder = BeanUtil.copy(repairOrderDto, RepairOrder.class);
				boolean isSuccess = repairOrderService.addRepairOrder(repairOrder);
				if(isSuccess){
					return 	Response.ok(0,id,"成功");
				}
				return Response.status(isSuccess);
			}else{
				String userId = SecureUtil.getUserId();
				repairOrderDto.setUpdatedBy(userId);
				repairOrderDto.setUpdatedTime(LocalDateTime.now());
				RepairOrder repairOrder = BeanUtil.copy(repairOrderDto, RepairOrder.class);
				return Response.status(repairOrderService.updateRepairOrder(id, repairOrder));
			}
		}else{
			//硬件
			if(""==id||null==id){  //没有id是新增
				id=IdWorker.get32UUID();
				code=System.currentTimeMillis()+"";
				repairOrderDto.setId(id);
				repairOrderDto.setCode(code);
				String userId = SecureUtil.getUserId();
				repairOrderDto.setCreatedBy(userId);
				repairOrderDto.setCreatedName(SecureUtil.getUserName());
				repairOrderDto.setRepairUsrname(xjrBaseUserController.getUserInfo(repairOrderDto.getRepairUsrid()).get(0).getUserSimpleInfoVo().getRealName());
				repairOrderDto.setCreatedTime(LocalDateTime.now());
				repairOrderDto.setAssignTime(LocalDateTime.now());
				RepairOrder repairOrder = BeanUtil.copy(repairOrderDto, RepairOrder.class);
				boolean isSuccess = repairOrderService.addRepairOrder(repairOrder);
				if(isSuccess){
					XjrBaseUser user = userService.getById(repairOrderDto.getRepairUsrid());
					//新增成功后发送钉钉消息
					try {
						DingTalkSendMsg.sendMsg(user.getDingTalkId(),repairOrderDto.getRepairUsrname());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					return 	Response.ok(0,id,"成功");
				}
				return Response.status(isSuccess);
			}else{
				String userId = SecureUtil.getUserId();
				repairOrderDto.setUpdatedBy(userId);
				repairOrderDto.setUpdatedTime(LocalDateTime.now());
				RepairOrder repairOrder = BeanUtil.copy(repairOrderDto, RepairOrder.class);
				return Response.status(repairOrderService.updateRepairOrder(id, repairOrder));
			}
		}

	}

	/**
	 * 修改 维修工单表
	 */
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "传入repairOrder")
	public Response update(@PathVariable String id, @RequestBody SaveRepairOrderFormDataDto formDto) {
		RepairOrderDto repairOrderDto = formDto.getRepairOrderDto();
		RepairOrder repairOrder = BeanUtil.copy(repairOrderDto, RepairOrder.class);
		boolean isSuccess =repairOrderService.updateRepairOrder(id, repairOrder);
		if(isSuccess){
			return 	Response.ok(0,id,"成功");
		}
		return Response.status(isSuccess);
	}

	/**
	 * 修改 维修工单表
	 */
	@PostMapping("/modify")
	@ApiOperation(value = "修改", notes = "传入repairOrder")
	public Response update( @RequestBody SaveRepairOrderFormDataDto formDto) {
		RepairOrderDto repairOrderDto = formDto.getRepairOrderDto();
		RepairOrder repairOrder = BeanUtil.copy(repairOrderDto, RepairOrder.class);
		return Response.status(repairOrderService.updateRepairOrder(repairOrderDto.getId(), repairOrder));
	}


	
	/**
	 * 删除 维修工单表
	 */
	@PostMapping("/del")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response delete(@RequestParam String ids) {
		return Response.status(repairOrderService.removeByIds(Func.toStrList(ids)));
	}

	/**
	 * 删除 维修工单表
	 */
	@DeleteMapping("/{ids}")
	@ApiOperation(value = "删除", notes = "传入ids")
	public Response remove(@ApiParam(value = "主键集合", required = true) @PathVariable String ids) {
		return Response.status(repairOrderService.removeByIds(Func.toStrList(ids)));
	}

	/**
	 *
	 * @param repairOrderDto
	 * @return
	 */
	@PostMapping("/getdata")
	@ApiOperation(value = "根据传入参数获取数据", notes = "传入指定参数")
	public String getDataList(@RequestBody RepairOrderDto repairOrderDto) {
		repairOrderDto.setSize(1000000000);
		IPage<RepairOrder> page = repairOrderService.getDataList(repairOrderDto);
		List<RepairOrder> records = BeanUtil.copyList(page.getRecords(), RepairOrder.class);
		// 转换列表数据
		DataTransUtil.transListShowData(records);
		String jsonString = JSON.toJSONString(records);
		System.out.println(jsonString);
		return  jsonString;
	}

	@GetMapping("/updaterepair")
	@ApiOperation(value = "重新指派", notes = "传入工单id，userid,username")
	public boolean updaterepair(@RequestParam String id , @RequestParam String userid,@RequestParam String username) {
		boolean updaterepair = repairOrderService.updateRepairUser(id,userid,username);
		if(updaterepair){
			XjrBaseUser user = userService.getById(userid);
			//重新指派后发送钉钉消息
			try {
				DingTalkSendMsg.sendMsg(user.getDingTalkId(),username);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return updaterepair;
	}

	/**
	 * 根据id修改工单状态  result_des
	 */
	@GetMapping("/updateStatusById")
	@ApiOperation(value = "根据id修改工单状态", notes = "传入ids")
	public Response updateStatusById(@ApiParam(value = "主键集合", required = true) @RequestParam String id , @RequestParam String status, @RequestParam(required=false)
	String fau_id, @RequestParam(required=false) String fau_name, @RequestParam(required=false) String resultdes,
									 @RequestParam(required=false) String machine_sn,@RequestParam(required=false) String handle_type,@RequestParam(required=false) String reason) {
		return Response.status(repairOrderService.updateStatusById(id,Integer.parseInt(status),fau_id,fau_name,resultdes,machine_sn,handle_type,reason));
	}

}
