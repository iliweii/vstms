<template>
  <a-card>
    <!-- 时间轴 -->
    <a-timeline class="fu-timeline" v-if="dataSource.length" :loading="confirmLoading">
      <a-timeline-item v-for="fu in dataSource" :key="fu.id">
        <pre class="pre"><b v-if="fu.type">【{{ fu.type }}】<br/></b>{{ fu.remarks }}</pre>
        {{ fu.createUser }} <a-divider type="vertical" /> {{ fu.createTime }} <a-divider type="vertical" />
        <a v-if="fu.filePath" @click="handleDownload(fu)" :title="fu.filePath.substr(fu.filePath.lastIndexOf('/') + 1)">
          下载附件 ({{ fu.filePath.substr(fu.filePath.lastIndexOf('/') + 1) }})
        </a>
      </a-timeline-item>
    </a-timeline>
    <!-- 空状态 -->
    <a-empty v-else />
    <!-- 按钮区域 -->
    <a-col :md="24" :style="{ display: 'flex', justifyContent: 'center' }">
      <a-button
        @click="showModal"
        :loading="confirmLoading"
        type="primary"
        style="padding: 5px 20px"
        :disabled="disabled"
        >新增</a-button
      >
      <!-- 完成按钮 -->
      <div :style="{ marginLeft: '40px' }">
        <a-button @click="() => handleSubmit(object.id)" type="primary" :disabled="disabled" icon="check"
          >完成调研过程上传</a-button
        >
        <div class="ant-form-extra" :style="{ fontSize: '12px' }">
          完成后方可执行后续步骤（调研报告），完成后不可撤销
        </div>
      </div>
    </a-col>
    <!-- 模态框 -->
    <a-modal v-model="visible" title="新增调研过程" @ok="handleOk" :width="800">
      <!-- 表单区域 -->
      <a-form :form="form" autocomplete="off" v-if="!disabled">
        <a-row>
          <a-col :md="24">
            <a-form-item :label-col="{ span: 3 }" :wrapper-col="{ span: 6 }" label="过程主题">
              <a-input
                placeholder="类型将加粗展示"
                :maxLength="40"
                v-decorator="['type', { rules: [{ required: false, message: '' }] }]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="24">
            <a-form-item :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }" label="过程内容">
              <a-textarea
                placeholder="请输入过程内容"
                :rows="4"
                :maxLength="450"
                v-decorator="['remarks', { rules: [{ required: true, message: '' }] }]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="24">
            <a-form-item prop="file" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }" label="添加附件">
              <j-upload
                style="width: 100%"
                :returnUrl="false"
                v-decorator="['file', { rules: [{ required: false, message: '' }] }]"
                fileType="file"
                :params="{ biz: 'temp' }"
                :number="1"
                @change="fileChange"
              ></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script>
import { putAction, httpAction, downloadFile } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import pick from 'lodash.pick'

export default {
  name: 'ResearchProcess',
  mixins: [JeecgListMixin],
  components: {},
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
  data() {
    return {
      // 标识变量
      visible: false, // 模态框展示标识
      confirmLoading: false, // 通用加载标识

      /* 排序参数 */
      isorter: {
        column: 'createTime',
        order: 'desc',
      },
      // 表单变量
      model: {},
      queryParam: { reqId: this.object.id },
      form: this.$form.createForm(this),
      url: {
        add: '/research/researchProcess/add', // 调研过程新增
        edit: '/research/researchProcess/edit', // 调研过程编辑
        list: '/research/researchProcess/list', // 调研过程列表
        delete: '/research/researchProcess/delete', // 调研过程删除
        reqEdit: '/research/researchReq/edit', // 需求调研编辑
      },
      // 表头
      columns: [
        {
          title: '时间',
          align: 'center',
          dataIndex: 'createTime',
          width: 140,
          ellipsis: true,
        },
        {
          title: '调研内容',
          align: 'left',
          dataIndex: 'remarks',
          width: 600,
          ellipsis: true,
        },
        {
          title: '附件',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 120,
        },
        {
          title: '提交人',
          align: 'center',
          dataIndex: 'createBy_dictText',
          width: 120,
          ellipsis: true,
        },
      ],
    }
  },
  methods: {
    /**
     * 展示模态框
     */
    showModal() {
      this.visible = true
    },
    /**
     * “编辑”方法。
     * @param record Task对象
     */
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.setNextTick(record)
    },
    /**
     * 文件下载
     */
    handleDownload: function (record) {
      let url = window._CONFIG['staticDomainURL'] + '/' + record.filePath
      let filename = record.filePath.substr(record.filePath.lastIndexOf('/') + 1)
      downloadFile(url, filename, {})
    },
    /**
     * “确认”事件
     * 编辑或新增，将数据提交到接口
     */
    handleOk() {
      const that = this
      if (!that.object.id) {
        this.$message.warning('请先保存基本信息')
        return
      }
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          // 赋值formData
          let formData = Object.assign(this.model, values)
          formData = Object.assign(formData, this.queryParam)
          // 请求
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                this.form.resetFields()
                that.loadData()
                // 关闭模态框
                this.visible = false
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
    /**
     * “返回”事件
     */
    handleCancel() {
      this.$emit('close')
    },
    /**
     * 增量“编辑”方法。
     * @param record 增量对象
     */
    incrementEdit(record) {
      this.model = Object.assign(this.model, record)
      this.setNextTick(record)
    },
    /**
     * 执行$nextTick => setFieldsValue
     * @param record 这条记录
     */
    setNextTick(record) {
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(record, 'type', 'remarks', 'file'))
      })
    },
    /**
     * 文件列表变化事件
     * @param file 文件list
     */
    fileChange(file) {
      if (file.length > 0) {
        this.model.filePath = file[0].filePath
      } else {
        this.model.filePath = ''
      }
    },
    handleSubmit(id) {
      const that = this
      putAction(that.url.reqEdit, { id, status: 3 }).then((res) => {
        that.$message.success('提交成功')
        that.object.status = '3'
        that.$emit('change', that.object)
      })
    },
  },
}
</script>
<style scoped lang="less">
@import '~@assets/less/common.less';
/deep/.fu-timeline {
  padding: 20px 40px;
  max-height: 70vh;
  overflow-x: hidden;
  overflow-y: scroll;
}
.pre {
  max-width: 60%;
  border: rgba(0, 0, 0, 0.35) solid 1px;
  padding: 15px 25px;
  border-radius: 6px;
  // 自动换行
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
