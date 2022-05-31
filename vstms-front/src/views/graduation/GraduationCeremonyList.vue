<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="培训班编号">
                <j-input placeholder="请输入培训班编号" v-model="queryParam.classNo"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="典礼状态">
                <j-search-select-tag
                  v-model="queryParam.status"
                  :triggerChange="true"
                  placeholder="请选择典礼状态"
                  dict="training_opening_ceremony_status"
                  :disabled="false"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                <a @click="handleToggleSearch" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                </a>
              </span>
            </a-col>

            <template v-if="toggleSearchStatus"> </template>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('结业典礼')">导出</a-button>
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
        </a-dropdown> -->
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
            <!-- 未确认 -->
            <template v-if="record.status == '0'">
              <a @click="handleConfirm(record)">确认典礼</a>
            </template>

            <!-- 已完成 -->
            <template v-else-if="record.status == '3'">
              <a @click="handleDetail(record)">详情</a>
            </template>

            <!-- 进行中 -->
            <template v-else>
              <a @click="handleEdit(record)">编辑</a>

              <a-divider v-if="record.status == '1'" type="vertical" />
              <a-dropdown v-if="record.status == '1'">
                <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
                <a-menu slot="overlay">
                  <a-menu-item>
                    <a-popconfirm title="确定取消确认典礼吗?" @confirm="() => handleCancelConfirm(record)">
                      <a>取消确认</a>
                    </a-popconfirm>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </template>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <graduationCeremony-modal ref="modalForm" @ok="modalFormOk"></graduationCeremony-modal>
    </a-card>
  </page-layout>
</template>

<script>
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import GraduationCeremonyModal from './modules/GraduationCeremonyBaseModal'
import { putAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'GraduationCeremonyList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    GraduationCeremonyModal,
  },
  data() {
    return {
      description: '结业典礼管理页面',
      title: '结业典礼',
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
          title: '培训班编号',
          align: 'center',
          dataIndex: 'classNo',
        },
        {
          title: '培训班名称',
          align: 'center',
          dataIndex: 'classNo_dictText',
        },
        {
          title: '举行日期',
          align: 'center',
          dataIndex: 'holdDate',
        },
        {
          title: '实际举行日期',
          align: 'center',
          dataIndex: 'realDate',
        },
        {
          title: '参与人数',
          align: 'center',
          dataIndex: 'referNum',
        },
        {
          title: '实际参与人数',
          align: 'center',
          dataIndex: 'realNum',
        },
        {
          title: '典礼状态',
          align: 'center',
          dataIndex: 'status_dictText',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/graduation/graduationCeremony/list',
        edit: '/graduation/graduationCeremony/edit',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    /**
     * 确认典礼
     */
    handleConfirm(param) {
      const that = this
      param.status = '1'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('确认成功')
        that.loadData()
      })
    },
    /**
     * 取消确认典礼
     */
    handleCancelConfirm(param) {
      const that = this
      param.status = '0'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('取消确认成功')
        that.loadData()
      })
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>