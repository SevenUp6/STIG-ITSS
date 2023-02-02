package com.xjrsoft.module.buildCode.grant;

import com.xjrsoft.core.tool.utils.CollectionUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.module.buildCode.dto.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppViewCodeGenerator {

    public static String genListViewCode(String tbPk, AppListViewSettingDto colModel, List<AppQueryFieldDto> queryModels, List<AppFieldConfigDto> formFieldList, BaseInfoDto baseModel) {
        StringBuilder code = new StringBuilder();
        String importStr = "";

        code.append("<template>\r\n");
        code.append("    <view class=\"page\" :style=\"[{ paddingTop: pagePaddingTop + 'px'}]\">\r\n");

        /* 顶部开始 */
        code.append("        <view class=\"cu-bar bg-white text-center fixed grid col-2 border-bottom\" :style=\"[{ top: CustomBar + 'px'}]\">\r\n");
        code.append("           <view @click.stop=\"showModal\" data-target=\"sortModal\">\r\n");
        code.append("               <text class=\"s15\">排序</text>\r\n");
        code.append("               <text class=\"cuIcon-sort\"></text>\r\n");
        code.append("           </view>\r\n");
        code.append("           <view @click.stop=\"showModal\" data-target=\"filterModal\">\r\n");
        code.append("               <text class=\"s15\">筛选</text>\r\n");
        code.append("               <text class=\"cuIcon-sort\"></text>\r\n");
        code.append("           </view>\r\n");
        code.append("        </view>\r\n");
        /* 顶部结束 */

        /* 列表开始 */
        code.append("        <view class=\"cu-list menu-avatar\">\r\n");
        code.append("           <view class=\"bg-white padding margin-sm radius-xs\" v-for=\"(item, index) in dataList\" :key=\"index\">\r\n");
        code.append("               <view class=\"content text-gray \">\r\n");
        code.append("                   <view>\r\n");
        boolean isImportedSetting = false;
        for(AppListViewColumnDto col : colModel.getListViewColumnDtoList()) {
            String type = col.getType();
            String fieldName = col.getField();
            Map<String, Object> listStyle = col.getListStyle();
            code.append("                       <view style=\"line-height: " + MapUtils.getInteger(listStyle, "lineHeight")*2 + "upx;\">\r\n");
            String[] fontStyle = StringUtils.split(MapUtils.getString(listStyle, "fontStyle"), StringPool.SPACE);
            if (StringUtil.equalsIgnoreCase(MapUtils.getString(listStyle, "showName"), "1")) {
                code.append("                           <text class=\"con-label\" style=\"font-style:" + fontStyle[0] + ";font-weight: " + (StringUtil.equalsIgnoreCase(fontStyle[1], "bold") ? fontStyle[1] : "normal") + ";font-size: " + MapUtils.getInteger(listStyle, "fontSize") * 2 + "upx;color: " + MapUtils.getString(listStyle, "fontColor") + ";\">" + col.getName() + "</text><text>：</text>\r\n");
            }
            if (StringUtil.equalsIgnoreCase(type, "image")) {
                code.append("                           <view v-for=\"it in item." + fieldName + "ImgList\" :key=\"it.F_Id\" class=\"imgListPic\" @click=\"previewImg(baseURL+ it.url)\">\r\n");
                code.append("                       	    <image :src=\"baseURL + it.url\" mode=\"aspectFill\"></image>\r\n");
                code.append("                           </view>\r\n");
                code.append("                       </view>\r\n");
                if (!isImportedSetting) {
                    isImportedSetting = true;
                }
            } else if (StringUtil.equalsIgnoreCase(type, "file")) {
                code.append("                           <view v-for=\"(it, index) in item." + fieldName + "FileList\" :key=\"index\" class=\"fileListbox\">\r\n");
                code.append("                               <!-- #ifdef H5 -->\r\n");
                code.append("                               <a :href=\"baseURL +it.url\" target=\"blank\">{{ it.F_FileName }}</a>\r\n");
                code.append("                               <!-- #endif -->\r\n");
                code.append("                               <!-- #ifdef APP-PLUS -->\r\n");
                code.append("                               <view @click=\"downLoad(baseUrl + item.url)\">{{ it.F_FileName }}</view>\r\n");
                code.append("                               <!-- #endif -->\r\n");
                code.append("                           </view>\r\n");
                code.append("                       </view>\r\n");
                if (!isImportedSetting) {
                    isImportedSetting = true;
                }
            } else {
                String[] fontStyle1 = StringUtils.split(MapUtils.getString(listStyle, "fontStyle1"), StringPool.SPACE);
                code.append("                           <text class=\"con-value\" style=\"font-style:" + fontStyle1[0]  + ";font-weight: " + (StringUtil.equalsIgnoreCase(fontStyle1[1], "bold") ? fontStyle1[1] : "normal")  + ";font-size: " + MapUtils.getInteger(listStyle, "fontSize1")*2 + "upx; color: " + MapUtils.getString(listStyle, "fontColor1")+ ";text-align: " + MapUtils.getString(listStyle, "textAlign")+ ";\">\r\n");
                code.append("                               <text " + (StringUtil.equalsIgnoreCase(MapUtils.getString(listStyle, "addTag"), "1") ? "style=\"background: " + MapUtils.getString(listStyle, "tagColor") + "; border-radius: 4px; display: inline-block; padding: 0 10px;\"" : "") + ">{{item." + col.getField() + "}}</text>\r\n");
                code.append("                            </text>\r\n");
                code.append("                       </view>\r\n");
            }
        }

        if (isImportedSetting) {
            importStr += "import { setting } from '@/appsetting.js'\r\n";
        }

        code.append("                   </view>\r\n");
        code.append("               </view>\r\n");

        code.append("               <view class=\"flex align-center justify-end text-blue padding-top-sm cu-list grid col-4\">\r\n");
        code.append("                   <view class=\"text-right\" @click=\"tapToForm(item." + tbPk + ")\"><text class=\"cuIcon-writefill margin-right-xs\"></text> 编辑</view>\r\n");
        code.append("                   <view class=\"text-right\" @click=\"tapToDelete(item." + tbPk + ")\"><text class=\"fa fa-trash margin-right-xs\"></text> 删除</view>\r\n");
        code.append("                   <view class=\"text-right\" @click=\"tapToDetail(item." + tbPk + ",index)\">查看 <text class=\"cuIcon-right\"></text></view>\r\n");
        code.append("               </view>\r\n");

        code.append("           </view>\r\n");
        code.append("        </view>\r\n");
        /* 列表结束 */


        /* 排序开始 */
        code.append("        <view class=\"cu-modal bottom-modal\" :class=\"modalName == 'sortModal' ? 'show' : ''\" :style=\"[{ top: pagePaddingTop + CustomBar + 'px'}]\">\r\n");

        code.append("            <view class=\"cu-dialog\">\r\n");
        code.append("                <view class=\"contentBox sort\">\r\n");
        code.append("                  <view class=\"text-bold text-grey\">排序方式</view>\r\n");
        code.append("                    <view class=\"text-grey margin-top-xs margin-bottom\">\r\n");
        code.append("                       <view class=\"divider_bottom padding-top-sm padding-bottom-sm padding-left-sm flex justify-between\" @click.stop=\"sortTypeSelect\" data-target=\"asc\">\r\n");
        code.append("                           <view>正序排序</view>\r\n");
        code.append("                           <view class=\"cuIcon-check text-bold text-blue\" :class=\"pageParam.order == 'asc' ? 'show' : 'hide'\"></view>\r\n");
        code.append("                       </view>\r\n");
        code.append("                       <view class=\"divider_bottom padding-top-sm padding-bottom-sm padding-left-sm flex justify-between\" @click.stop=\"sortTypeSelect\" data-target=\"desc\">\r\n");
        code.append("                           <view>倒序排序</view>\r\n");
        code.append("                           <view class=\"cuIcon-check text-bold text-blue\" :class=\"pageParam.order == 'desc' ? 'show' : 'hide'\"></view>\r\n");
        code.append("                       </view>\r\n");
        code.append("                    </view>\r\n");
        code.append("                   <view class=\"text-bold text-grey\">排序属性</view>\r\n");
        code.append("                   <view class=\"text-grey margin-top-xs margin-bottom\">\r\n");


        for(AppQueryFieldDto queryModel : queryModels)
        {

            code.append("                       <view class=\"divider_bottom padding-top-sm padding-bottom-sm padding-left-sm flex justify-between\" @click.stop=\"sortSelect\" data-target=\""+queryModel.getField()+"\">\r\n");
            code.append("                           <view>"+queryModel.getName()+"</view>\r\n");
            code.append("                           <view class=\"cuIcon-check text-bold text-blue\" :class=\"pageParam.orderfield == '"+queryModel.getField()+"' ? 'show' : 'hide'\"></view>\r\n");
            code.append("                       </view>\r\n");

        }

        code.append("                   </view>\r\n");
        code.append("                </view>\r\n");
        code.append("                <view class=\"grid col-2 btnbox border-top\">\r\n");
        code.append("                    <view class=\"line-blue\" @tap=\"reset\">重置</view>\r\n");
        code.append("                    <view class=\"bg-blue\" @tap=\"confirm\">确定</view>\r\n");
        code.append("                </view>\r\n");
        code.append("           </view>\r\n");
        code.append("        </view>\r\n");
        /* 排序结束 */


        /* 筛选开始 */
        code.append("        <view class=\"cu-modal bottom-modal\" :class=\"modalName == 'filterModal' ? 'show' : ''\" :style=\"[{ top: pagePaddingTop + CustomBar + 'px'}]\">\r\n");
        code.append("            <view class=\"cu-dialog bg-white\">\r\n");
        code.append("                <view class=\"contentBox\">\r\n");


        String dataStr = "";
        String componentStr = "";
        String timeSearchStr = StringPool.EMPTY;
        for(AppQueryFieldDto queryModel : queryModels) {
            switch(queryModel.getType()) {
                case "xjrTimeSearch":
                    dataStr += queryModel.getField() + "DateConfig : { 'title' : '" + queryModel.getName() + "' },";
                    if (!StringUtils.contains(importStr, "Date.vue")) {
                        importStr += "import xjrDate from \"@/components/Filter/Date/Date.vue\" \r\n";
                        componentStr += "xjrDate, \r\n";
                    }
                    code.append("                        <xjr-date :dateConfig=\"" + queryModel.getField() + "DateConfig\"  v-model=\"pageParam['" + queryModel.getField() + "']\"></xjr-date>\r\n");
                    timeSearchStr += String.format("            const %1$s_daterange =  this.pageParam[\"%1$s\"] \r\n", queryModel.getField());
                    timeSearchStr += String.format("            this.pageParam[\"%1$s_Start\"] = %1$s_daterange[0] \r\n", queryModel.getField());
                    timeSearchStr += String.format("            this.pageParam[\"%1$s_End\"] = %1$s_daterange[1] \r\n", queryModel.getField());
                    break;
                case "date":
                    dataStr += queryModel.getField() + "DateConfig : { 'title' : '" + queryModel.getName() + "' },";
                    if (!StringUtils.contains(importStr, "Date.vue")) {
                        importStr += "import xjrDate from \"@/components/Filter/Date/Date.vue\" \r\n";
                        componentStr += "xjrDate, \r\n";
                    }
                    code.append("                        <xjr-date :dateConfig=\"" + queryModel.getField() + "DateConfig\"  v-model=\"pageParam['" + queryModel.getField() + "']\"></xjr-date>\r\n");
                    timeSearchStr += String.format("            const %1$s_daterange =  this.pageParam[\"%1$s\"] \r\n", queryModel.getField());
                    timeSearchStr += String.format("            this.pageParam[\"%1$s_Start\"] = %1$s_daterange[0] \r\n", queryModel.getField());
                    timeSearchStr += String.format("            this.pageParam[\"%1$s_End\"] = %1$s_daterange[1] \r\n", queryModel.getField());
                    break;
                case "input":
                    dataStr += queryModel.getField() + "InputConfig : { 'title' : '" + queryModel.getName() + "','placeholder' : '请输入" + queryModel.getName() + "' },";
                    if (!StringUtils.contains(importStr, "Input.vue")) {
                        importStr += "import xjrInput from \"@/components/Filter/Input/Input.vue\" \r\n";
                        componentStr += "xjrInput, \r\n";
                    }
                    code.append("                        <xjr-input :inputConfig=\"" + queryModel.getField() + "InputConfig\" v-model=\"pageParam['" + queryModel.getField() + "']\"></xjr-input>\r\n");
                    break;
                case "radio":
                    //找到表单设定里面的参数
                    AppFieldConfigDto formConfig = formFieldList.stream().filter(x -> StringUtil.equals(x.getField(), queryModel.getField())).findAny().orElse(null);
                    dataStr += queryModel.getField() + "SingleConfig : { 'title' : '" + queryModel.getName() + "','placeholder' : '请输入" + queryModel.getName() + "','dataSource' : '" + formConfig.getDataSource() + "', 'dataItem':'" + formConfig.getDataItem() + "','saveField' : '" + formConfig.getSaveField() + "','showField':'" + formConfig.getShowField() + "' },";
                    if (!StringUtils.contains(importStr, "Single.vue")) {
                        importStr += "import xjrSingle from \"@/components/Filter/Single/Single.vue\" \r\n";
                        componentStr += "xjrSingle, \r\n";
                    }
                    code.append("                        <xjr-single :singleConfig=\"" + queryModel.getField() + "SingleConfig\" v-model=\"pageParam['" + queryModel.getField() + "']\"></xjr-single>\r\n");
                    break;
                case "checkbox":
                    //找到表单设定里面的参数
                    AppFieldConfigDto multiformConfig = formFieldList.stream().filter(x -> StringUtil.equals(x.getField(), queryModel.getField())).findAny().orElse(null);
                    dataStr += queryModel.getField() + "MultipleConfig : { 'title' : '" + queryModel.getName() + "','placeholder' : '请输入" + queryModel.getName() + "','dataSource' : '" + multiformConfig.getDataSource() + "', 'dataItem':'" + multiformConfig.getDataItem() + "','saveField' : '" + multiformConfig.getSaveField() + "','showField':'" + multiformConfig.getShowField() + "' },";
                    if (!StringUtils.contains(importStr, "Multiple.vue")) {
                        importStr += "import xjrMultiple from \"@/components/Filter/Multiple/Multiple.vue\" \r\n";
                        componentStr += "xjrMultiple, \r\n";
                    }
                    code.append("                        <xjr-multiple :multipleConfig=\"" + queryModel.getField() + "MultipleConfig\" v-model=\"pageParam['" + queryModel.getField() + "']\"></xjr-multiple>\r\n");
                    break;
                case "select":
                    //找到表单设定里面的参数
                    AppFieldConfigDto selectformConfig = formFieldList.stream().filter(x -> StringUtil.equals(x.getField(), queryModel.getField())).findAny().orElse(null);
                    dataStr += queryModel.getField() + "SelectConfig : { 'title' : '" + queryModel.getName() + "','placeholder' : '请输入" + queryModel.getName() + "','dataSource' : '" + selectformConfig.getDataSource() + "', 'dataItem':'" + selectformConfig.getDataItem() + "','saveField' : '" + selectformConfig.getSaveField() + "','showField':'" + selectformConfig.getShowField() + "' },";
                    if (!StringUtils.contains(importStr, "Picker.vue")) {
                        importStr += "import xjrSelect from \"@/components/Filter/Picker/Picker.vue\" \r\n";
                        componentStr += "xjrSelect, \r\n";
                    }
                    code.append("                        <xjr-select :selectConfig=\"" + queryModel.getField() + "SelectConfig\" v-model=\"pageParam['" + queryModel.getField() + "']\"></xjr-select>\r\n");
                    break;
                default:
                    dataStr += queryModel.getField() + "InputConfig : { 'title' : '" + queryModel.getName() + "','placeholder' : '请输入" + queryModel.getName() + "' },";
                    if (!StringUtils.contains(importStr, "Input.vue")) {
                        importStr += "import xjrInput from \"@/components/Filter/Input/Input.vue\" \r\n";
                        componentStr += "xjrInput, \r\n";
                    }
                    code.append("                        <xjr-input :inputConfig=\"" + queryModel.getField() + "InputConfig\" v-model=\"pageParam['" + queryModel.getField() + "']\"></xjr-input>\r\n");
                    break;

            }

        }

        code.append("                </view>\r\n");
        code.append("                <view class=\"grid col-2 btnbox border-top\">\r\n");
        code.append("                    <view class=\"line-blue\" @tap=\"reset\">重置</view>\r\n");
        code.append("                    <view class=\"bg-blue\" @tap=\"confirm\">确定</view>\r\n");
        code.append("                </view>\r\n");
        code.append("           </view>\r\n");
        code.append("        </view>\r\n");
        /* 筛选结束 */

        code.append("    <view class=\"cuIcon-roundaddfill text-blue\" @click=\"tapToFormAdd()\"></view>\r\n");
        code.append("    </view>\r\n");
        code.append("</template>\r\n\r\n");

        code.append("<script>\r\n");

        code.append(importStr);

        code.append("export default {\r\n");
        code.append("    data() {\r\n");
        code.append("        return {\r\n");
        code.append("            baseURL:setting.baseURL,\r\n");
        code.append("            StatusBar: this.StatusBar,\r\n");
        code.append("            CustomBar: this.CustomBar,\r\n");
        code.append("            pagePaddingTop: this.pagePaddingTop,\r\n");
        code.append("            modalName: null,\r\n");
        code.append("            dataList:[],\r\n");
        code.append("            curID:'',\r\n");
        code.append("            controlType:'',\r\n");
        code.append("            pageParam: { limit: 1,size: 15,order: '',orderfield: '' },\r\n");
        code.append("            listTotal: 0,\r\n");

        //放置各种组件配置
        code.append(dataStr);
        //放置各种组件配置结束

        code.append("        }\r\n");
        code.append("    },\r\n");

        code.append("    onShow() {\r\n");
        code.append("        if(this.controlType==\"add\"){\r\n");
        code.append("            this.pageParam.limit = 1;\r\n");
        code.append("            this.dataList = []\r\n");
        code.append("        }\r\n");
        code.append("        this.getData()\r\n");
        code.append("    },\r\n");

        code.append("    onReachBottom() {\r\n");
        code.append("        if (this.pageParam.limit < this.listTotal) {\r\n");
        code.append("            this.pageParam.limit += 1;\r\n");
        code.append("            this.getData();\r\n");
        code.append("        }else{\r\n");
        code.append("            uni.showToast({\r\n");
        code.append("                    title: '没有更多数据了~',\r\n");
        code.append("                    icon:'none',\r\n");
        code.append("                    duration: 2000,\r\n");
        code.append("                    position:'bottom'\r\n");
        code.append("            });\r\n");
        code.append("        }\r\n");
        code.append("    },\r\n");

        code.append("    methods: {\r\n");
        code.append("        downLoad(url) {\r\n");
        code.append("            uni.request({\r\n");
        code.append("                    url: url,\r\n");
        code.append("                    method: 'HEAD',\r\n");
        code.append("                    success: function(response) {\r\n");
        code.append("                let header = response.header\r\n");
        code.append("                let filename = ''\r\n");
        code.append("               if (header['Content-Disposition'] && (header['Content-Disposition'].indexOf(\"filename*=UTF-8''\") >= 0)) {\r\n");
        code.append("                   let headerArr = header['Content-Disposition'].split(\"filename*=UTF-8''\")\r\n");
        code.append("                    filename = headerArr[1]\r\n");
        code.append("                } else {\r\n");
        code.append("                    let headerArr = header['Content-Disposition'].split(\"filename=\")\r\n");
        code.append("                    filename = headerArr[1]\r\n");
        code.append("                }\r\n");
        code.append("                let dtask = plus.downloader.createDownload(url, {\r\n");
        code.append("                        filename: \"_downloads/\" + decodeURIComponent(filename)\r\n");
        code.append("           }, function(download, status) {\r\n");
        code.append("                    if (status == 200) {\r\n");
        code.append("                        plus.runtime.openFile(download.filename)\r\n");
        code.append("                    } else {\r\n");
        code.append("                        //下载失败\r\n");
        code.append("                        plus.downloader.clear(); //清除下载任务\r\n");
        code.append("                    }\r\n");
        code.append("                })\r\n");
        code.append("                dtask.start()\r\n");
        code.append("            }\r\n");
        code.append("        })\r\n");
        code.append("        },\r\n");
        code.append("        navBack(obj) {\r\n");
        code.append("            this.curID=obj." + tbPk + ";\r\n");
        code.append("        },\r\n");
        code.append("        tapToForm(id) {\r\n");
        code.append("            this.controlType='edit';\r\n");
        code.append("            this.curID=id;\r\n");
        code.append("            uni.navigateTo({\r\n");
        code.append("                url: \"./" + baseModel.getName() + "Form?keyValue=\" + id + \"&type=edit\" \r\n");
        code.append("            })\r\n");
        code.append("        },\r\n");
        code.append("        tapToFormAdd(id) {\r\n");
        code.append("        this.controlType='add';\r\n");
        code.append("            uni.navigateTo({\r\n");
        code.append("                url: \"./" + baseModel.getName() + "Form?type=edit\" \r\n");
        code.append("            })\r\n");
        code.append("        },\r\n");
        code.append("        tapToDetail(id) {\r\n");
        code.append("            uni.navigateTo({\r\n");
        code.append("                url: \"./" + baseModel.getName() + "Form?keyValue=\" + id + \"&type=look\" \r\n");
        code.append("            })\r\n");
        code.append("        },\r\n");
        code.append("        tapToDelete(id,index) {\r\n");
        code.append("            const that = this;\r\n");
        code.append("            uni.showModal({\r\n");
        code.append("                cancelText: '取消',\r\n");
        code.append("                confirmText: '确定',\r\n");
        code.append("                content: '是否确定删除此数据？',\r\n");
        code.append("                success: async (res) => {\r\n");
        code.append("                    if (res.confirm) {\r\n");
        code.append("                       const { data } = await that.$http.request({\r\n");
        code.append("                           url: '/" + baseModel.getName() + "/' + id,\r\n");
        code.append("                           method: 'DELETE'\r\n");
        code.append("                       });\r\n");
        code.append("                       let idx=this.dataList.findIndex(o=>{return o." + tbPk + "==id})\r\n");
        code.append("                       if(idx>=0){\r\n");
        code.append("                           this.dataList.splice(idx,1)\r\n");
        code.append("                       }\r\n");
        code.append("                    } else {\r\n");
        code.append("                        return;\r\n");
        code.append("                    }\r\n");
        code.append("                }\r\n");
        code.append("            });\r\n");
        code.append("        },\r\n");
        code.append("        showModal(e) {\r\n");
        code.append("            if (this.modalName == e.currentTarget.dataset.target)\r\n");
        code.append("               this.modalName = null;\r\n");
        code.append("            else\r\n");
        code.append("               this.modalName = e.currentTarget.dataset.target\r\n");
        code.append("        },\r\n");
        code.append("        confirm(e) {\r\n");
        code.append("            this.modalName = null;\r\n");
        code.append("            this.pageParam.limit=1;\r\n");
        code.append("            this.dataList=[]\r\n");
        code.append("            this.controlType='';\r\n");
        code.append("            this.getData();\r\n");
        code.append("        },\r\n");
        code.append("        reset(e) { \r\n");
        code.append("            for (let key in this.pageParam) {\r\n");
        code.append("               if (key == 'limit') \r\n");
        code.append("                   this.pageParam[key] = 1;\r\n");
        code.append("               else if (key == 'size') \r\n");
        code.append("                   this.pageParam[key] = 15;\r\n");
        code.append("               else\r\n");
        code.append("                   this.$set(this.pageParam,key,null);\r\n");
        code.append("            }\r\n");
        code.append("        },\r\n");
        code.append("        sortTypeSelect(e) {\r\n");
        code.append("            if (this.pageParam.order == e.currentTarget.dataset.target) {\r\n");
        code.append("               this.pageParam.order = '';\r\n");
        code.append("            } else {\r\n");
        code.append("               this.pageParam.order = e.currentTarget.dataset.target\r\n");
        code.append("            }\r\n");
        code.append("        },\r\n");
        code.append("        sortSelect(e) {\r\n");
        code.append("            if (this.pageParam.orderfield == e.currentTarget.dataset.target) {\r\n");
        code.append("               this.pageParam.orderfield = '';\r\n");
        code.append("            } else {\r\n");
        code.append("               this.pageParam.orderfield = e.currentTarget.dataset.target\r\n");
        code.append("            }\r\n");
        code.append("        },\r\n");
        code.append("        async getData(){\r\n");
        code.append("           const {\r\n");
        code.append("               data: {\r\n");
        code.append("                   Rows,\r\n");
        code.append("                   Total\r\n");
        code.append("               }\r\n");
        code.append("           } = await this.$http.request({\r\n");
        code.append("               url: '/" + baseModel.getName() + "',\r\n");
        code.append("               method: 'get',\r\n");
        code.append("               data: this.pageParam\r\n");
        code.append("           });\r\n");
        code.append("           if (this.controlType==\"edit\") {\r\n");
        code.append("                Rows.forEach(item=>{\r\n");
        for (AppListViewColumnDto column : colModel.getListViewColumnDtoList()) {
            String imgField = column.getField();
            String type = column.getType();
            if (StringUtil.equalsIgnoreCase(type, "image")) {
                code.append("                   this.getImgFun(item,item." + imgField + ",'" + imgField + "ImgList')\r\n");
            }
            if (StringUtil.equalsIgnoreCase(type, "file")) {
                code.append("                   this.getImgFun(item,item." + imgField + ",'" + imgField + "FileList')\r\n");
            }
        }
        code.append("                })\r\n");
        code.append("                let temp=Rows.find(o => {\r\n");
        code.append("                    return o."+ tbPk +" == this.curID\r\n");
        code.append("                })\r\n");
        code.append("                let idx = this.dataList.findIndex(o => {\r\n");
        code.append("                    return o."+ tbPk +" == this.curID\r\n");
        code.append("                })\r\n");
        code.append("                if (idx >= 0) {\r\n");
        code.append("                    this.dataList.splice(idx, 1, temp)\r\n");
        code.append("                }\r\n");
        code.append("           } else {\r\n");
        code.append("                this.listTotal=Math.ceil(Total / this.pageParam.size);\r\n");
        code.append("                this.dataList = this.dataList.concat(Rows);\r\n");
        code.append("                this.dataList.forEach(item=>{\r\n");
        for (AppListViewColumnDto column : colModel.getListViewColumnDtoList()) {
            String imgField = column.getField();
            String type = column.getType();
            if (StringUtil.equalsIgnoreCase(type, "image")) {
                code.append("                   this.getImgFun(item,item." + imgField + ",'" + imgField + "ImgList')\r\n");
            }
            if (StringUtil.equalsIgnoreCase(type, "file")) {
                code.append("                   this.getImgFun(item,item." + imgField + ",'" + imgField + "FileList')\r\n");
            }
        }
        code.append("                })\r\n");
        code.append("           }\r\n");
        code.append("        },\r\n");

        code.append("    async getImgFun(item, imgField, name){\r\n");
        code.append("       if (imgField) {\r\n");
        code.append("           const {\r\n");
        code.append("               data\r\n");
        code.append("           }=await this.$http.request({\r\n");
        code.append("                   url: '/annexes-files/url/' + imgField,\r\n");
        code.append("                   method: 'get'\r\n");
        code.append("    	    })\r\n");
        code.append("           if ( data && data.length > 0) {\r\n");
        code.append("               this.$set(item, name, data)\r\n");
        code.append("           }\r\n");
        code.append("       }\r\n");
        code.append("    },\r\n");
        code.append("    previewImg(url){\r\n");
        code.append("        let arr=[]\r\n");
        code.append("        arr.push(url)\r\n");
        code.append("        uni.previewImage({\r\n");
        code.append("                urls: arr,\r\n");
        code.append("                longPressActions: {\r\n");
        code.append("            itemList: ['发送给朋友', '保存图片', '收藏'],\r\n");
        code.append("            success: function(data) {\r\n");
        code.append("                //console.log('选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data.index + 1) + '张图片');\r\n");
        code.append("            },\r\n");
        code.append("            fail: function(err) {\r\n");
        code.append("                //console.log(err.errMsg);\r\n");
        code.append("            }\r\n");
        code.append("        }\r\n");
        code.append("    		});\r\n");
        code.append("    }\r\n");
        code.append("    },\r\n");

        code.append("    components: {\r\n");
        code.append(componentStr);
        code.append("    }\r\n");
        code.append("}\r\n\r\n");
        code.append("</script>\r\n");

        code.append("<style>");
        code.append("\r\n\t.cu-modal {");
        code.append("\r\n\t\ttransform: scale(1);");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.cu-dialog {");
        code.append("\r\n\t\theight: 100%;");
        code.append("\r\n\t\tposition: relative;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.btnbox {");
        code.append("\r\n\t\tposition: absolute;");
        code.append("\r\n\t\tbottom: 0;");
        code.append("\r\n\t\twidth: 100%;");
        code.append("\r\n\t\theight: 100upx;");
        code.append("\r\n\t\tline-height: 100upx;");
        code.append("\r\n\t\tfont-size: 32upx;");
        code.append("\r\n\t\tbackground-color: #fff;");
        code.append("\r\n\t\tbox-sizing: border-box;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.contentBox {");
        code.append("\r\n\t\theight: calc(100% - 100upx);");
        code.append("\r\n\t\toverflow: auto;");
        code.append("\r\n\t\ttext-align: left;");
        code.append("\r\n\t\tpadding: 40upx 10upx;");
        code.append("\r\n\t\tbackground-color: #fff;");
        code.append("\r\n\t}");
        code.append("\r\n\t");
        code.append("\r\n\t.contentBox.sort {");
        code.append("\r\n\t\tpadding: 40upx;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.show {");
        code.append("\r\n\t\tdisplay: block;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.hide {");
        code.append("\r\n\t\tdisplay: none;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.cuIcon-roundaddfill {");
        code.append("\r\n\t\tfont-size: 100upx;");
        code.append("\r\n\t\tright: 20upx;");
        code.append("\r\n\t\tbottom: 100upx;");
        code.append("\r\n\t\tposition: fixed;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.cu-bar.fixed {");
        code.append("\r\n\t\tbox-shadow: none;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.divider_bottom {");
        code.append("\r\n\t\tborder-bottom: 1px solid #f0f0f0;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.s15 {");
        code.append("\r\n\t\tfont-size: 30upx;");
        code.append("\r\n\t}\r\n");
        code.append(".imgListPic{\r\n");
        code.append("    width: 200upx;\r\n");
        code.append("    height: 200upx;\r\n");
        code.append("    display: inline-block;\r\n");
        code.append("    margin-right: 20upx;\r\n");
        code.append("    vertical-align: middle;\r\n");
        code.append("}\r\n");
        code.append(".imgListPic uni-image{\r\n");
        code.append("    width: 100%;\r\n");
        code.append("    height: 100%;\r\n");
        code.append("    display: block;\r\n");
        code.append("}\r\n");
        code.append(".fileListbox{\r\n");
        code.append("   background-color: #e9fef8;\r\n");
        code.append("   border: 1px solid #b0f1e1;\r\n");
        code.append("   color: #00bb8e;\r\n");
        code.append("   border-radius: 5px;\r\n");
        code.append("   line-height: 14px;\r\n");
        code.append("   padding: 5px 10px;\r\n");
        code.append("   margin: 10px 0;\r\n");
        code.append("   font-size: 12px;\r\n");
        code.append("   word-break: break-all;\r\n");
        code.append("}\r\n");
        code.append(".fileListbox a{\r\n");
        code.append("    color: #00bb8e;\r\n");
        code.append("}\r\n");
        code.append(".con-label{\r\n");
        code.append("    display: inline-block;\r\n");
        code.append("    width: 120upx;\r\n");
        code.append("    vertical-align: middle;\r\n");
        code.append("}\r\n");
        code.append(".con-value{\r\n");
        code.append("    display: inline-block;\r\n");
        code.append("    width: calc(100% - 150upx);\r\n");
        code.append("    vertical-align: middle;\r\n");
        code.append("}\r\n");
        code.append("\r\n</style>");

        return code.toString();
    }

    public static String genFormViewCode(List<DbTableDto> dbTableModelList, List<AppFieldConfigDto> formFieldList, BaseInfoDto baseModel) {
        StringBuilder code = new StringBuilder();

        code.append("<template>\r\n");
        code.append("    <view class=\"padding-bottom\">\r\n");

        code.append("        <form :model=\"formData\"> \r\n");

        String configStr = StringPool.EMPTY; //各组件配置
        String importStr = StringPool.EMPTY; //导入组件
        String ruleStr = StringPool.EMPTY; //验证
        String componentStr = StringPool.EMPTY; //注册
        String formDataStr = StringPool.EMPTY;//提交对象
        String formatterStr = StringPool.EMPTY;//获取表单数据 格式化
        String childTableRuleStr = StringPool.EMPTY;//子表单验证
        String validateStr = StringPool.EMPTY;//提交验证
        String mainTableName = StringPool.EMPTY;

        // 字段默认值
        Map<String, Map<String, Object>> defaultValueMap = new HashMap<>();

        List<String> addedTableList = new ArrayList<>(); //已经加了的表格名 集合
            for(AppFieldConfigDto component : formFieldList) {
                // 默认值
                Object defaultValue = component.getDefaultValue();
                if (!StringUtil.isEmpty(defaultValue)) {
                    String tableName = component.getTable();
                    Map<String, Object> map = defaultValueMap.get(tableName);
                    if (map == null) {
                        map = new HashMap<>();
                        defaultValueMap.put(tableName, map);
                    }
                    map.put(component.getField(), defaultValue);
                }

                //如果验证信息不为空
                if (!StringUtil.isEmpty(component.getVerify())) {
                    ruleStr += "{name : '" + component.getField() + "',type:'" + component.getVerify() + "',title:'" + component.getName() + "'},";
                }
                //找到当前组件所属表 对象
                DbTableDto tableModel = dbTableModelList.stream().filter(x -> StringUtil.equals(x.getName(), component.getTable())).findAny().orElse(null);
                String modelStr = StringPool.EMPTY;
                if (!StringUtil.isEmpty(component.getTable())) {
                    modelStr = tableModel.getParentName() == 1 ? component.getTable() + "Entity" : component.getTable() + "EntityList";
                }

                switch (component.getType()) {
                    case "title":
                        code.append("            <xjr-title :titleConfig=\"" + component.getField() + "titleConfig\"></xjr-title> \r\n");
                        configStr += component.getField() + "titleConfig : { 'text':'" + component.getName() + "' },";
                        if (!StringUtils.contains(importStr, "xjrTitle")) {
                            importStr += "import xjrTitle from \"@/components/form/Title/Title.vue\" \r\n";
                            componentStr += "xjrTitle, \r\n";
                        }
                        break;
                    case "input":
                        code.append("            <xjr-input v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :inputConfig=\"" + component.getField() + "inputConfig\" :disabled=\"type=='look'?true:false\"></xjr-input> \r\n");
                        configStr += component.getField() + "inputConfig : { 'title':'" + component.getName() + "',placeholder : '请填写"+component.getName() + "' },";
                        if (!StringUtils.contains(importStr, "xjrInput")) {
                            importStr += "import xjrInput from \"@/components/form/Input/Input.vue\" \r\n";
                            componentStr += "xjrInput, \r\n";
                        }
                        break;
                    case "textarea":
                        code.append("            <xjr-text-area v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :textAreaConfig=\"" + component.getField() + "textAreaConfig\" :disabled=\"type=='look'?true:false\"></xjr-text-area> \r\n");
                        configStr += component.getField() + "textAreaConfig : { 'title':'" + component.getName() + "',placeholder : '请填写" + component.getName() + "' },";
                        if (!StringUtils.contains(importStr, "xjrTextArea")) {
                            importStr += "import xjrTextArea from \"@/components/form/TextArea/TextArea.vue\" \r\n";
                            componentStr += "xjrTextArea, \r\n";
                        }

                        break;
                    case "radio":
                        code.append("            <xjr-radio v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :radioConfig=\"" + component.getField() + "radioConfig\" :disabled=\"type=='look'?true:false\"></xjr-radio> \r\n");
                        configStr += component.getField() + "radioConfig : { 'title':'" + component.getName() + "',dataSource:'" + component.getDataSource() + "',dataItem:'" + component.getDataItem() + "',showfield:'" + component.getShowField() + "',savefield:'" + component.getSaveField() + "' },";
                        if (!StringUtils.contains(importStr, "xjrRadio")) {
                            importStr += "import xjrRadio from \"@/components/form/Radio/Radio.vue\" \r\n";
                            componentStr += "xjrRadio, \r\n";
                        }

                        break;
                    case "checkbox":
                        code.append("            <xjr-check-box v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :checkboxConfig=\"" + component.getField() + "checkboxConfig\" :disabled=\"type=='look'?true:false\"></xjr-check-box> \r\n");
                        configStr += component.getField() + "checkboxConfig : { 'title':'" + component.getName() + "',dataSource:'" + component.getDataSource() + "',dataItem:'" + component.getDataItem() + "',showfield:'" + component.getShowField() + "',savefield:'" + component.getSaveField() + "' },";
                        if (!StringUtils.contains(importStr, "xjrCheckBox")) {
                            importStr += "import xjrCheckBox from \"@/components/form/Checked/Checked.vue\" \r\n";
                            componentStr += "xjrCheckBox, \r\n";
                        }

                        break;
                    case "select":
                        code.append("            <xjr-select v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :selectConfig=\"" + component.getField() + "selectConfig\" :disabled=\"type=='look'?true:false\"></xjr-select> \r\n");
                        configStr += component.getField() + "selectConfig : { 'title':'" + component.getName() + "',dataSource:'" + component.getDataSource() + "',dataItem:'" + component.getDataItem() + "',showfield:'" + component.getShowField() + "',savefield:'" + component.getSaveField() + "' },";
                        if (!StringUtils.contains(importStr, "xjrSelect")) {
                            importStr += "import xjrSelect from \"@/components/form/Select/Select.vue\" \r\n";
                            componentStr += "xjrSelect, \r\n";
                        }

                        break;
                    case "date":
                        code.append("            <xjr-date-picker v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :datePickerConfig=\"" + component.getField() + "datePickerConfig\" :disabled=\"type=='look'?true:false\"></xjr-date-picker> \r\n");
                        configStr += component.getField() + "datePickerConfig : { 'title':'" + component.getName() + "',placeholder : '请选择" + component.getName() + "', format:'" + component.getDateFormat() + "' },";
                        if (!StringUtils.contains(importStr, "xjrDatePicker")) {
                            importStr += "import xjrDatePicker from \"@/components/form/DatePicker/DatePicker.vue\" \r\n";
                            componentStr += "xjrDatePicker, \r\n";
                        }

                        break;
                    case "time":
                        code.append("            <xjr-time-picker v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :timePickerConfig=\"" + component.getField() + "timePickerConfig\" :disabled=\"type=='look'?true:false\"></xjr-time-picker> \r\n");
                        configStr += component.getField() + "timePickerConfig : { 'title':'" + component.getName() + "',placeholder : '请选择" + component.getName() + "' },";
                        if (!StringUtils.contains(importStr, "xjrTimePicker")) {
                            importStr += "import xjrTimePicker from \"@/components/form/TimePicker/TimePicker.vue\" \r\n";
                            componentStr += "xjrTimePicker, \r\n";

                        }

                        break;
                    case "daterange":
                        code.append("            <xjr-date-time-range-picker v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :datetimePickerConfig=\"" + component.getField() + "datetimePickerConfig\" :disabled=\"type=='look'?true:false\"></xjr-date-time-range-picker> \r\n");
                        configStr += component.getField() + "datetimePickerConfig : { 'title':'" + component.getName() + "',placeholder : '请选择" + component.getName() + "', format: '" + component.getDateFormat() + "'},";
                        if (!StringUtils.contains(importStr, "xjrDateTimeRangePicker")) {
                            importStr += "import xjrDateTimeRangePicker from \"@/components/form/DateTimePicker/DateTimePicker.vue\" \r\n";
                            componentStr += "xjrDateTimeRangePicker, \r\n";
                        }

                        break;
                    case "edit":
                        code.append("            <xjr-edit v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :editConfig=\"" + component.getField() + "editConfig\" :disabled=\"type=='look'?true:false\"></xjr-edit> \r\n");
                        configStr += component.getField() + "editConfig : { 'text':'" + component.getName() + "' },";
                        if (!StringUtils.contains(importStr, "xjrEdit")) {
                            importStr += "import xjrEdit from \"@/components/form/Editor/Editor.vue\" \r\n";
                            componentStr += "xjrEdit, \r\n";
                        }

                        break;
                    case "file":
                        code.append("            <xjr-file-up-load v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :fileUploadConfig=\"" + component.getField() + "fileUploadConfig\" :disabled=\"type=='look'?true:false\"></xjr-file-up-load> \r\n");
                        configStr += component.getField() + "fileUploadConfig : { 'text':'" + component.getName() + "',placeholder : '请选择" + component.getName() + "' },";
                        if (!StringUtils.contains(importStr, "xjrFileUpLoad")) {
                            importStr += "import xjrFileUpLoad from \"@/components/form/FileUpload/FileUpload.vue\" \r\n";
                            componentStr += "xjrFileUpLoad, \r\n";
                        }

                        break;
                    case "image":
                        code.append("            <xjr-img-up-load v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :imgUpLoadConfig=\"" + component.getField() + "imgUpLoadConfig\" :disabled=\"type=='look'?true:false\"></xjr-img-up-load> \r\n");
                        configStr += component.getField() + "imgUpLoadConfig : { 'title':'" + component.getName() + "',placeholder : '请选择" + component.getName() + "', imgLength: " + component.getImgLength() + "},";
                        if (!StringUtils.contains(importStr, "xjrImgUpLoad")) {
                            importStr += "import xjrImgUpLoad from \"@/components/form/Img/Img.vue\" \r\n";
                            componentStr += "xjrImgUpLoad, \r\n";
                        }

                        break;
                    case "info":
                        code.append("            <xjr-info v-model=\"formData." + modelStr + "['" + component.getField() + "']\" :infoConfig=\"" + component.getField() + "infoConfig\"></xjr-info> \r\n");
                        configStr += component.getField() + "infoConfig : { 'text':'" + component.getName() + "',type : '" + component.getInfo() + "' },";
                        if (!StringUtils.contains(importStr, "xjrInfo")) {
                            importStr += "import xjrInfo from \"@/components/form/Info/Info.vue\" \r\n";
                            componentStr += "xjrInfo, \r\n";
                        }

                        break;
                    case "table": //如果是表格
                        //便利表格所有列配置
                        code.append("            <br>\r\n");
                        code.append("            <view class=\"formBox\">\r\n");
                        code.append("            <view  class=\"title\">" + component.getName() + "表格</view>\r\n");
                        code.append("            <view v-for=\"(item, index) in formData." + component.getTable() + "EntityList\" :key=\"index\">\r\n");
                        code.append("                <view class=\"flex justify-between padding bg-gray\">\r\n");
                        code.append("                    <view>编辑（第{{index+1}}行）</view>\r\n");
                        code.append("                    <view v-if=\"formData." + component.getTable() + "EntityList.length > 1 && type == 'edit'\" @tap=\"deleteTable('" + component.getTable() + "EntityList',index)\" class=\"text-blue\">删除</view>\r\n");
                        code.append("                </view>\r\n");


                        String childrule = StringPool.EMPTY;

                        for (AppSubFromFieldDto columnInfo : component.getOptionDto().getColumnList()) {
                        //如果验证信息不为空
                        if (!StringUtil.isEmpty(columnInfo.getVerify())) {
                            childrule += "{name : '" + columnInfo.getProp() + "',type:'" + columnInfo.getVerify() + "',title:'" + columnInfo.getLabel() + "'},";
                        }

                        switch (columnInfo.getType().get("value")) {
                            case "input":
                                code.append("            <xjr-input v-model=\"item['" + columnInfo.getProp() + "']\" :inputConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "inputConfig\" :disabled=\"type=='look'?true:false\"></xjr-input> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "inputConfig : { 'title':'" + columnInfo.getLabel() + "',placeholder : '请填写" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrInput")) {
                                    importStr += "import xjrInput from \"@/components/form/Input/Input.vue\" \r\n";
                                    componentStr += "xjrInput, \r\n";
                                }
                                break;
                            case "textarea":
                                code.append("            <xjr-text-area v-model=\"item['" + columnInfo.getProp() + "']\" :textAreaConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "textAreaConfig\" :disabled=\"type=='look'?true:false\"></xjr-text-area> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "textAreaConfig : { 'title':'" + columnInfo.getLabel() + "',placeholder : '请填写" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrTextArea")) {
                                    importStr += "import xjrTextArea from \"@/components/form/TextArea/TextArea.vue\" \r\n";
                                    componentStr += "xjrTextArea, \r\n";
                                }
                                break;
                            case "radio":
                                code.append("            <xjr-radio v-model=\"item['" + columnInfo.getProp() + "']\" :radioConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "radioConfig\" :disabled=\"type=='look'?true:false\"></xjr-radio> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "radioConfig : { 'title':'" + columnInfo.getLabel() + "',dataSource:'" + columnInfo.getDataSource() + "',dataItem:'" + columnInfo.getDataItem() + "',showfield:'" + columnInfo.getShowField() + "',savefield:'" + columnInfo.getSaveField() + "' },";
                                if (!StringUtils.contains(importStr, "xjrRadio"))
                                {
                                    importStr += "import xjrRadio from \"@/components/form/Radio/Radio.vue\" \r\n";
                                    componentStr += "xjrRadio, \r\n";
                                }
                                break;
                            case "checkbox":
                                code.append("            <xjr-check-box v-model=\"item['" + columnInfo.getProp() + "']\" :checkboxConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "checkboxConfig\" :disabled=\"type=='look'?true:false\"></xjr-check-box> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "checkboxConfig : { 'title':'" + columnInfo.getLabel() + "',dataSource:'" + columnInfo.getDataSource() + "',dataItem:'" + columnInfo.getDataItem() + "',showfield:'" + columnInfo.getShowField() + "',savefield:'" + columnInfo.getSaveField() + "' },";
                                if (!StringUtils.contains(importStr, "xjrCheckBox")) {
                                    importStr += "import xjrCheckBox from \"@/components/form/Checked/Checked.vue\" \r\n";
                                    componentStr += "xjrCheckBox, \r\n";
                                }
                                break;
                            case "select":
                                code.append("            <xjr-select v-model=\"item['" + columnInfo.getProp() + "']\" :selectConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "selectConfig\" :disabled=\"type=='look'?true:false\"></xjr-select> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "selectConfig : { 'title':'" + columnInfo.getLabel() + "',dataSource:'" + columnInfo.getDataSource() + "',dataItem:'" + columnInfo.getDataItem() + "',showfield:'" + columnInfo.getShowField() + "',savefield:'" + columnInfo.getSaveField() + "' },";
                                if (!StringUtils.contains(importStr, "xjrSelect")) {
                                    importStr += "import xjrSelect from \"@/components/form/Select/Select.vue\" \r\n";
                                    componentStr += "xjrSelect, \r\n";
                                }
                                break;
                            case "date":
                                code.append("            <xjr-date-picker v-model=\"item['" + columnInfo.getProp() + "']\" :datePickerConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "datePickerConfig\" :disabled=\"type=='look'?true:false\"></xjr-date-picker> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "datePickerConfig : { 'title':'" + columnInfo.getLabel() + "',placeholder : '请选择" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrDatePicker")) {
                                    importStr += "import xjrDatePicker from \"@/components/form/DatePicker/DatePicker.vue\" \r\n";
                                    componentStr += "xjrDatePicker, \r\n";
                                }
                                break;
                            case "time":
                                code.append("            <xjr-time-picker v-model=\"item['" + columnInfo.getProp() + "']\" :timePickerConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "timePickerConfig\" :disabled=\"type=='look'?true:false\"></xjr-time-picker> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "timePickerConfig : { 'title':'" + columnInfo.getLabel() + "',placeholder : '请选择" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrTimePicker")) {
                                    importStr += "import xjrTimePicker from \"@/components/form/TimePicker/TimePicker.vue\" \r\n";
                                    componentStr += "xjrTimePicker, \r\n";
                                }
                                break;
                            case "daterange":
                                code.append("            <xjr-date-time-range-picker v-model=\"item['" + columnInfo.getProp() + "']\" :datetimePickerConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "datetimePickerConfig\" :disabled=\"type=='look'?true:false\"></xjr-date-time-range-picker> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "datetimePickerConfig : { 'title':'" + columnInfo.getLabel() + "',placeholder : '请选择" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrDateTimeRangePicker")) {
                                    importStr += "import xjrDateTimeRangePicker from \"@/components/form/DateTimePicker/DateTimePicker.vue\" \r\n";
                                    componentStr += "xjrDateTimeRangePicker, \r\n";
                                }
                                break;
                            case "edit":
                                code.append("            <xjr-edit v-model=\"item['" + columnInfo.getProp() + "']\" :editConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "editConfig\" :disabled=\"type=='look'?true:false\"></xjr-edit> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "editConfig : { 'text':'" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrEdit")) {
                                    importStr += "import xjrEdit from \"@/components/form/Editor/Editor.vue\" \r\n";
                                    componentStr += "xjrEdit, \r\n";
                                }
                                break;
                            case "file":
                                code.append("            <xjr-file-up-load v-model=\"item['" + columnInfo.getProp() + "']\" :fileUploadConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "fileUploadConfig\" :disabled=\"type=='look'?true:false\"></xjr-file-up-load> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "fileUploadConfig : { 'text':'" + columnInfo.getLabel() + "',placeholder : '请选择" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrFileUpLoad")) {
                                    importStr += "import xjrFileUpLoad from \"@/components/form/FileUpload/FileUpload.vue\" \r\n";
                                    componentStr += "xjrFileUpLoad, \r\n";
                                }
                                break;
                            case "image":
                                code.append("            <xjr-img-up-load v-model=\"item['" + columnInfo.getProp() + "']\" :imgUpLoadConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "imgUpLoadConfig\" :disabled=\"type=='look'?true:false\"></xjr-img-up-load> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "imgUpLoadConfig : { 'text':'" + columnInfo.getLabel() + "',placeholder : '请选择" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrImgUpLoad")) {
                                    importStr += "import xjrImgUpLoad from \"@/components/form/Img/Img.vue\" \r\n";
                                    componentStr += "xjrImgUpLoad, \r\n";
                                }
                                break;
                            case "info":
                                code.append("            <xjr-info v-model=\"item['" + columnInfo.getProp() + "']\" :infoConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "infoConfig\"></xjr-info> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "infoConfig : { 'text':'" + columnInfo.getLabel() + "',type : '" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrInfo")) {
                                    importStr += "import xjrInfo from \"@/components/form/Info/Info.vue\" \r\n";
                                    componentStr += "xjrInfo, \r\n";
                                }
                                break;
                            default:
                                code.append("            <xjr-input v-model=\"item['" + columnInfo.getProp() + "']\" :inputConfig=\"" + component.getTable() + "_" + columnInfo.getProp() + "inputConfig\" :disabled=\"type=='look'?true:false\"></xjr-input> \r\n");
                                configStr += component.getTable() + "_" + columnInfo.getProp() + "inputConfig : { 'title':'" + columnInfo.getLabel() + "',placeholder : '请填写" + columnInfo.getLabel() + "' },";
                                if (!StringUtils.contains(importStr, "xjrInput")) {
                                    importStr += "import xjrInput from \"@/components/form/Input/Input.vue\" \r\n";
                                    componentStr += "xjrInput, \r\n";
                                }
                                break;
                        }
                    }

                    childTableRuleStr += component.getTable() + "rules : [" + childrule + "],";

                    code.append("                <view v-if=\"formData." + component.getTable() + "EntityList.length == index + 1 && type == 'edit'\" class=\"padding text-center text-blue\">\r\n");
                    code.append("                    <view @tap=\"addTable('" + component.getTable() + "EntityList')\">+ 新增一行</view>\r\n");
                    code.append("                </view>\r\n");

                    code.append("            </view>\r\n");
                    code.append("            </view>\r\n");
                    code.append("            <br><br><br>\r\n");
                    break;
                }


                if (!StringUtil.isEmpty(component.getTable()) && !addedTableList.contains(component.getTable())) {
                    DbTableDto dbTableDto = dbTableModelList.stream().filter(x -> StringUtil.equals(x.getName(), component.getTable())).findAny().orElse(null);
                    if (dbTableDto != null) {
                        if (dbTableDto.getParentName() == 1) {
                            //主表提交验证逻辑 开始
                            validateStr += "            const checked = validate.validate(this.formData." + component.getTable() + "Entity, this.rules) //表单验证 \r\n";
                            validateStr += "            if (!checked.isOk) { \r\n";
                            validateStr += "                uni.showToast({ \r\n";
                            validateStr += "                    title: checked.errmsg, \r\n";
                            validateStr += "                    icon: 'none', \r\n";
                            validateStr += "                    position: 'top' \r\n";
                            validateStr += "                }); \r\n";
                            validateStr += "                return; \r\n";
                            //主表提交验证逻辑 结束

                        } else {
                            //子表提交验证逻辑 开始
                            validateStr += "            for(let i = 0;i <this.formData." + component.getTable() + "EntityList.length;i++){ \r\n";
                            validateStr += "                const checked = validate.validate(this.formData." + component.getTable() + "EntityList[i], this." + component.getTable() + "rules) //表单验证 \r\n";
                            validateStr += "                if (!checked.isOk) { \r\n";
                            validateStr += "                    uni.showToast({ \r\n";
                            validateStr += "                        title: '" + component.getName() + "第 ' + (i + 1) + \" 行\" + checked.errmsg, \r\n";
                            validateStr += "                        icon: 'none', \r\n";
                            validateStr += "                        position: 'top' \r\n";
                            validateStr += "                    }); \r\n";
                            validateStr += "                    return; \r\n";
                            validateStr += "                } \r\n";

                            //子表提交验证逻辑 结束

                        }
                    }
                    validateStr += "            } \r\n";

                    addedTableList.add(component.getTable());
                }

            }

        //formdata 字符串构建
        // 如果是主表 默认使用entity  如果是附表 使用 entitylist

        //如果是多表 返回值会跟单表不一样
        // 单表 直接返回 单表的FormOutputDto
        //多表 返回值是 多个表的实体类
        for (DbTableDto table : dbTableModelList) {
            String tableName = table.getName();

            // 默认值
            StringBuilder defaultValueStr = new StringBuilder();
            Map<String, Object> map = defaultValueMap.get(tableName);
            if (CollectionUtil.isNotEmpty(map)) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    defaultValueStr.append(entry.getKey()).append(": '").append(entry.getValue()).append("',");
                }
            }

            if (table.getParentName() == 1) {
                mainTableName = table.getName();
                formDataStr += table.getName() + "Entity : {" + defaultValueStr.toString() + "},";


                if (dbTableModelList.size() > 1) {
                    formatterStr += "this.formData." + table.getName() + "Entity = res.data." + table.getName() + " \r\n";
                } else {
                    formatterStr += "this.formData." + table.getName() + "Entity = res.data \r\n";
                }
            } else {
                formDataStr += table.getName() + "EntityList : [{" + defaultValueStr.toString() + "}],";

                if (dbTableModelList.size() > 1) {
                    formatterStr += "this.formData." + table.getName() + "EntityList = res.data." + table.getName() + ".length > 0 ? res.data." + table.getName() + " : [{}]\r\n";
                }
            }
        }

        code.append("        <button v-if=\"type == 'edit'\" class=\"bg-blue margin-lr margin-top\" @click=\"submit\">提交</button> \r\n");
        code.append("        </form> \r\n");
        code.append("    </view>\r\n");
        code.append("</template>\r\n\r\n");

        code.append("<script>\r\n");

        code.append("import validate from '@/js_sdk/fshjie-formvalidate/ys-validate.js'\r\n");
        code.append(importStr);

        code.append("export default {\r\n");
        code.append("    data() {\r\n");
        code.append("        return {\r\n");
        code.append(configStr);
        code.append("            formData: {\r\n");
        code.append("            " + formDataStr + "\r\n");
        code.append("            },\r\n");


        code.append("            rules : [" + ruleStr + "],\r\n");
        code.append(childTableRuleStr);


        code.append("            type: '',\r\n");
        code.append("            keyValue: false,\r\n");
        code.append("        }\r\n");
        code.append("    },\r\n");
        code.append("    onLoad(options) {\r\n");
        code.append("       this.type = options.type;\r\n");

        code.append("       if (options.keyValue) {\r\n");
        code.append("           this.keyValue = options.keyValue;\r\n");
        code.append("           this.$http.request({\r\n");
        code.append("               url: '/" + baseModel.getName() + "/' + options.keyValue,\r\n");
        code.append("               method: 'get'\r\n");
        code.append("           }).then(res => {\r\n");

        code.append(formatterStr);

        code.append("           });\r\n");

        code.append("       }\r\n");

        code.append("    },\r\n");
        code.append("    methods: {\r\n");
        code.append("        deleteTable(tableKey, index) {\r\n");
        code.append("            this.formData[tableKey].splice(index, 1);\r\n");
        code.append("        },\r\n");
        code.append("        addTable(tableKey) {\r\n");
        code.append("            this.formData[tableKey].push({});\r\n");
        code.append("        },\r\n");


        code.append("        submit() {\r\n");


        code.append(validateStr);

        code.append("            uni.showLoading({title: '提交中'}); \r\n");

        code.append("            if (this.keyValue) { \r\n");
        code.append("                this.$http.request({ \r\n");
        code.append("                    url: '/" + baseModel.getName() + "/' + this.keyValue, \r\n");
        code.append("                    method: 'put', \r\n");
        code.append("                    data: this.formData \r\n");
        code.append("                }).then(res=>{ \r\n");
        code.append("                    if(res.code){");
        code.append("                       uni.showToast({\r\n");
        code.append("                           title: '提交失败'\r\n");
        code.append("                        });\r\n");
        code.append("                    }else{\r\n");
        code.append("                       this.goBack()\r\n");
        code.append("                    }");
        code.append("                });\r\n");
        code.append("            } \r\n");
        code.append("            else { \r\n");
        code.append("                this.$http.request({ \r\n");
        code.append("                    url: '/" + baseModel.getName() + "', \r\n");
        code.append("                    method: 'post', \r\n");
        code.append("                    data: this.formData \r\n");
        code.append("                }).then(res=>{ \r\n");
        code.append("                    if(res.code){");
        code.append("                       uni.showToast({\r\n");
        code.append("                           title: '提交失败'\r\n");
        code.append("                        });\r\n");
        code.append("                    }else{\r\n");
        code.append("                       this.goBack()\r\n");
        code.append("                    }");
        code.append("                }); \r\n");
        code.append("            } \r\n");

        code.append("        },\r\n");
        code.append("        goBack(){\r\n");
        code.append("            uni.showToast({\r\n");
        code.append("                    title: '提交成功'\r\n");
        code.append("        		});\r\n");
        code.append("           uni.navigateBack({\r\n");
        code.append("                    success: () => {\r\n");
        code.append("                let pages = getCurrentPages(); //跳转页面成功之后\r\n");
        code.append("                let prevPage = pages[pages.length - 2];\r\n");
        code.append("                if (!prevPage) return;\r\n");
        code.append("               prevPage.navBack(this.formData." + mainTableName + "Entity);\r\n");
        code.append("            }\r\n");
        code.append("        		});\r\n");
        code.append("        }\r\n");
        code.append("    },\r\n");
        code.append("    components: {\r\n");
        code.append(componentStr);
        code.append("    },\r\n");




        code.append("};\r\n\r\n");
        code.append("</script>\r\n");

        code.append("<style scoped>");
        code.append("\r\n\t.title {");
        code.append("\r\n\t\tpadding: 10px;");
        code.append("\r\n\t\tfont-weight: bold;");
        code.append("\r\n\t}");
        code.append("\r\n");
        code.append("\r\n\t.formBox {");
        code.append("\r\n\t\tpadding: 10px;");
        code.append("\r\n\t\tbackground: rgba(0, 0, 0, .1);");
        code.append("\r\n\t\tborder-radius: 10px;");
        code.append("\r\n\t}");
        code.append("\r\n</style>");

        return code.toString();
    }
}
