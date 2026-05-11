# Practice 8 / Practice 9 Execution Plan

##Practice9 : 本文档按照老师 tutorial 的流程整理，先说明需要修改的代码，再说明后续如何执行、保存结果与现场展示。

## 1. Overview

### Practice 8 目标
- 给 Teedy 补充测试用例。
- 提升 JaCoCo 的 `instruction coverage` 和 `branch coverage`。
- 这次代码实现阶段只写测试与相关配置，不运行。

### Practice 9 目标
- 为项目补齐 `Jenkinsfile`。
- 让 Jenkins pipeline 能展示：
  - CI pipeline execution
  - Test results
  - Artifacts
  - Site documentation
- 这次代码实现阶段只准备代码、目录和说明，不运行 Jenkins。

## 2. Deliverables Directory Layout

##Practice9 : 所有需要给老师展示的材料统一放到 practicedocuments 下，避免之后生成结果时路径混乱。

```text
practicedocuments/
  practice8/
    README.md
    outputs/
      .gitkeep
  practice9/
    README.md
    outputs/
      .gitkeep
```

### Practice 8 结果保存约定
- `practicedocuments/practice8/outputs/original-jacoco/`
  - 保存修改前的 JaCoCo 报告。
- `practicedocuments/practice8/outputs/updated-jacoco/`
  - 保存新增测试后的 JaCoCo 报告。
- `practicedocuments/practice8/outputs/`
  - 保存测试命令输出摘要、截图、说明文本。

### Practice 9 结果保存约定
- `practicedocuments/practice9/outputs/pipeline/`
  - 保存 Jenkins pipeline 总览截图。
- `practicedocuments/practice9/outputs/test-results/`
  - 保存 Jenkins Test Result 截图。
- `practicedocuments/practice9/outputs/artifacts/`
  - 保存 Jenkins Artifacts 截图。
- `practicedocuments/practice9/outputs/site/`
  - 保存 `index.html` 入口截图与站点首页截图。

## 3. Practice 8 Implementation Plan

### 3.1 这次实际修改什么
- 扩展 `docs-core` 里的 `MimeTypeUtil` 相关测试。
- 扩展或新增 `docs-web-common` 里的 `ValidationUtil` 相关测试。
- 视分支覆盖需要，补充 `ResourceUtil` 的过滤分支测试。

### 3.2 为什么选这些类
- 这些类逻辑集中、分支明确、风险低。
- 不需要改动业务逻辑，只通过新增测试就可以提高覆盖率。
- 覆盖成功分支、默认分支、异常分支都比较方便。

### 3.3 计划补的测试点

#### A. `MimeTypeUtil.guessMimeType(Path file, String name)`
- 已有测试覆盖了常见文件类型识别。
- 还要补这些关键分支：
  - `Files.probeContentType(file)` 返回 `null`，但 `name` 可以回退识别。
  - `name == null` 时不能通过文件名回退。
  - `probeContentType == null` 且文件名也识别不出时，返回 `MimeType.DEFAULT`。

#### B. `MimeTypeUtil.getFileExtension(String mimeType)`
- 为所有显式 `case` 添加断言。
- 补 `default -> bin` 分支断言。

#### C. `ValidationUtil`
- `validateLength`
  - 去掉首尾空格后返回成功值。
  - `nullable == true` 且空值时直接返回。
  - `null`、长度过短、长度过长时抛异常。
- `validateTagName`
  - 正常标签名通过。
  - 含空格、含冒号时报错。
- `validateAlphanumeric`
  - 合法字符通过。
  - 含非法字符时报错。
- `validateUsername`
  - 用户名允许字母数字下划线以及 `@.-`。
  - 含非法字符时报错。
- `validateInteger` / `validateLong`
  - 合法数字解析成功。
  - 非法输入抛异常。
- `validateDate`
  - 合法毫秒时间戳解析成功。
  - `nullable == true` 且空值返回 `null`。
  - 非法时间字符串抛异常。

#### D. `ResourceUtil`
- 补 `FilenameFilter` 生效分支。
- 验证返回结果只保留符合过滤器的资源名。

### 3.4 Practice 8 后续执行命令

#### 先生成修改前报告
```bash
mvn clean test jacoco:report
```

#### 保存修改前报告
```bash
mkdir -p practicedocuments/practice8/outputs/original-jacoco
cp -R target/site/jacoco-aggregate practicedocuments/practice8/outputs/original-jacoco/
```

#### 单独执行新增测试
```bash
mvn -Dtest=TestMimeTypeUtil,TestValidationUtilExtended,TestResourceUtil test
```

#### 再生成修改后报告
```bash
mvn test jacoco:report
```

#### 保存修改后报告
```bash
mkdir -p practicedocuments/practice8/outputs/updated-jacoco
cp -R target/site/jacoco-aggregate practicedocuments/practice8/outputs/updated-jacoco/
```

### 3.5 Practice 8 现场展示顺序
1. 展示原始 JaCoCo 报告。
2. 展示新增测试代码。
3. 运行新增测试。
4. 再展示更新后的 JaCoCo 报告。
5. 指出 `instruction coverage` 和 `branch coverage` 都提高了。

## 4. Practice 9 Implementation Plan

### 4.1 这次实际修改什么
- 新增仓库根目录 `Jenkinsfile`。
- 在根 `pom.xml` 增加最小可用 `JaCoCo` 插件配置。
- 保留已有 `PMD` 和 `site` 基础配置，只做必要补充。

### 4.2 Jenkins Pipeline 结构
- `Checkout / Clean`
- `Compile`
- `Test`
- `PMD`
- `JaCoCo`
- `Site`
- `Package`
- `Javadoc` 作为可选项，不做硬性要求

### 4.3 Jenkins 里要展示什么

#### A. CI pipeline execution
- Jenkins Pipeline Overview 中要能看到各 stage。
- 老师主要看 pipeline 本身是否完整，不强制要求 `Javadoc stage`。

#### B. Test results
- Jenkins 要发布 JUnit 测试结果。
- 构建页面里要出现 `Test Result` 入口。

#### C. Artifacts
- Jenkins 要归档：
  - 根 `target/site/**`
  - `docs-core/target/**`
  - `docs-web-common/target/**`
  - `docs-web/target/**`
- 这样老师就可以在 Artifacts 页面看到二进制产物和站点文件。

#### D. Site documentation
- Jenkins 构建完成后，Artifacts 中应能找到 `target/site/index.html`。
- 点击 `index.html` 后，应能看到 Maven site 首页。

### 4.4 Practice 9 后续执行命令

#### 本地先手工验证 Maven 流程
```bash
mvn clean compile
mvn test
mvn pmd:check pmd:pmd
mvn jacoco:report
mvn site
mvn package
```

#### Jenkins 需要归档的主要路径
```text
target/site/**
docs-core/target/**
docs-web-common/target/**
docs-web/target/**
```

### 4.5 Practice 9 结果保存方式
- `practicedocuments/practice9/outputs/pipeline/`
  - 保存 Pipeline Overview 截图。
- `practicedocuments/practice9/outputs/test-results/`
  - 保存 Test Result 页面截图。
- `practicedocuments/practice9/outputs/artifacts/`
  - 保存 Build Artifacts 页面截图。
- `practicedocuments/practice9/outputs/site/`
  - 保存 `index.html` 文件位置截图和最终站点页面截图。

### 4.6 Practice 9 现场展示顺序
1. 展示 Jenkins pipeline 各 stage。
2. 打开 `Test Result` 页面。
3. 打开 `Build Artifacts` 页面，展示 jar / war / site 文件。
4. 点击 `target/site/index.html` 展示 site documentation。

## 5. Files To Inspect During Demo

### Practice 8
- 新增或修改的测试类
- `practicedocuments/practice8/outputs/original-jacoco/`
- `practicedocuments/practice8/outputs/updated-jacoco/`

### Practice 9
- `Jenkinsfile`
- 根 `pom.xml`
- `practicedocuments/practice9/outputs/`

## 6. Notes
- 当前阶段先实现代码，不运行。
- 真正运行后，再把老师要看的报告、截图和命令输出复制到 `practicedocuments` 对应目录。
- 所有新增或修改的代码都要带 `##Practice8 :` 或 `##Practice9 :` 开头的中文注释。
