<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="学员用户名">
              <j-input placeholder="请输入学员用户名" v-model="queryParam.student"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset({ classNo: object.classNo })"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus" :disabled="disabled">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('评选优秀学员')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <a-dropdown v-if="selectedRowKeys.length > 0" :disabled="disabled">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force-nowrap"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)" :disabled="disabled">
            <a :disabled="disabled">删除</a>
          </a-popconfirm>
        </span>
        <span slot="status" slot-scope="text">
          <a-tag color="cyan" v-if="text == '0'"> 候选优秀学员 </a-tag>
          <a-tag color="pink" v-else> 优秀学员 </a-tag>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 完成按钮 -->
    <div :style="{ marginTop: '15px' }">
      <a-button @click="() => handleSubmit(object)" type="primary" :disabled="disabled" icon="check"
        >完成评选优秀学员推荐上传</a-button
      >
      <div class="ant-form-extra" :style="{ fontSize: '12px' }">
        完成后方可执行后续步骤（评选优秀学员结果），完成后不可撤销
      </div>
    </div>

    <!-- 表单区域 -->
    <select-user-modal ref="selectUserModal" @selectFinished="selectOK" :key="String(new Date())"></select-user-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import SelectUserModal from './SelectUserModal1.vue'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { putAction } from '@/api/manage'

export default {
  name: 'GraduationSelectStudentList',
  mixins: [JeecgListMixin],
  components: {
    SelectUserModal,
  },
  data() {
    return {
      description: '评选优秀学员管理页面',
      queryParam: {
        classNo: this.object.classNo,
      },
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '学员用户名',
          align: 'center',
          dataIndex: 'student',
        },
        {
          title: '学员姓名',
          align: 'center',
          dataIndex: 'student_dictText',
        },
        {
          title: '评选状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/graduation/graduationSelectStudent/list?type=SelectionList',
        link: '/graduation/graduationSelectStudent/link',
        edit: '/graduation/graduationSelectClass/edit', // 编辑主表信息
        delete: '/graduation/graduationSelectStudent/delete',
        deleteBatch: '/graduation/graduationSelectStudent/deleteBatch',
      },
    }
  },
  props: {
    // 1. 对象数据
    object: Object,
    // 通用禁用标识
    disabled: {
      type: Boolean,
      default: true,
      required: false,
    },
  },
  methods: {
    handleAdd() {
      this.$refs.selectUserModal.queryParam = {
        classNo: this.object.classNo,
        type: 'student',
      }
      this.$refs.selectUserModal.visible = true
      this.$refs.selectUserModal.loadData()
    },
    selectOK(data) {
      let params = {}
      params.classNo = this.object.classNo
      params.usernameList = []
      for (var a = 0; a < data.length; a++) {
        params.usernameList.push(data[a])
      }
      putAction(this.url.link, params).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    handleSubmit(param) {
      const that = this
      param.status = '3'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('提交成功')
        that.object.status = '3'
        that.$emit('change', that.object)
      })
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>