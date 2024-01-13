classDiagram
direction BT
class AuthCode {
  + TableField~AuthCodeRecord, Instant~ CREATION_DATE
  - long serialVersionUID
  + TableField~AuthCodeRecord, String~ CODE
  + AuthCode AUTH_CODE
  + TableField~AuthCodeRecord, String~ EMAIL
  + rename(String) AuthCode
  + as(Table~?~) AuthCode
  + getSchema() Schema?
  + as(String) AuthCode
  + as(Name) AuthCode
  + mapping(Function3~String, String, Instant, U~) SelectField~U~
  + fieldsRow() Row3~String, String, Instant~
  + rename(Table~?~) AuthCode
  + rename(Name) AuthCode
  + getRecordType() Class~AuthCodeRecord~
  + mapping(Class~U~, Function3~String, String, Instant, U~) SelectField~U~
}
class AuthCodeDto {
  - String email
  - String code
  # canEqual(Object) boolean
  + equals(Object) boolean
  + setEmail(String) void
  + hashCode() int
  + getCode() String
  + setCode(String) void
  + toString() String
  + getEmail() String
}
class AuthCodeEntity {
  - String email
  - Instant creationDate
  - String code
  + toString() String
  + builder() AuthCodeEntityBuilder
  + getCode() String
  + setEmail(String) void
  + equals(Object) boolean
  + setCreationDate(Instant) void
  + getEmail() String
  + setCode(String) void
  # canEqual(Object) boolean
  + hashCode() int
  + getCreationDate() Instant
}
class AuthCodeRecord {
  - long serialVersionUID
  + component3() Instant
  + getEmail() String
  + field1() Field~String~
  + value3() Instant
  + setCode(String) void
  + value2(String) AuthCodeRecord
  + getCreationDate() Instant
  + component1() String
  + value2() String
  + valuesRow() Row3~String, String, Instant~
  + value1() String
  + field3() Field~Instant~
  + values(String, String, Instant) AuthCodeRecord
  + setEmail(String) void
  + field2() Field~String~
  + value1(String) AuthCodeRecord
  + component2() String
  + value3(Instant) AuthCodeRecord
  + getCode() String
  + setCreationDate(Instant) void
  + fieldsRow() Row3~String, String, Instant~
}
class AuthCodeRepository {
<<Interface>>
  + insert(AuthCodeEntity) void
  + deleteAllCodes(String) void
  + findByEmail(String) Optional~AuthCodeEntity~
}
class AuthCodeRepositoryImpl {
  - Logger log
  - DSLContext dsl
  + findByEmail(String) Optional~AuthCodeEntity~
  + deleteAllCodes(String) void
  + insert(AuthCodeEntity) void
}
class AuthController {
<<Interface>>
  + create(UserCreateDto, BindingResult) ResponseEntity~?~
  + sendCode(String) ResponseEntity~?~
  + login(AuthCodeDto, BindingResult) ResponseEntity~?~
}
class AuthControllerImpl {
  - Logger log
  - AuthService authService
  + create(UserCreateDto, BindingResult) ResponseEntity~?~
  + login(AuthCodeDto, BindingResult) ResponseEntity~?~
  + sendCode(String) ResponseEntity~?~
}
class AuthService {
<<Interface>>
  + sendEmailCode(String) void
  + login(AuthCodeDto) boolean
}
class AuthServiceImpl {
  - UserRepository userRepository
  - Logger log
  - EmailService emailService
  - Integer MIN_NUM
  - AuthCodeRepository authCodeRepository
  - Integer MAX_NUM
  - checkAuthCode(AuthCodeEntity, String, String) void
  + login(AuthCodeDto) boolean
  + sendEmailCode(String) void
}
class CollectionResponseDto {
  ~ MetaDto meta
  ~ Object data
  + setData(Object) void
  # canEqual(Object) boolean
  + getMeta() MetaDto
  + equals(Object) boolean
  + toString() String
  + setMeta(MetaDto) void
  + getData() Object
  + hashCode() int
}
class ControllerUrls {
  + String LOGIN_URL
  + String USER_URL
  + String USER_ID_URL
  + String LOGIN_CODE_URL
}
class Converter {
  + getErrorsMap(BindingResult) ErrorsMap
}
class DefaultCatalog {
  - long serialVersionUID
  + DefaultCatalog DEFAULT_CATALOG
  + Public PUBLIC
  - String REQUIRE_RUNTIME_JOOQ_VERSION
  + getSchemas() List~Schema~
}
class DslConfiguration {
  - DataSource dataSource
  ~ SQLDialect dialect
  + connectionProvider() DataSourceConnectionProvider
  + exceptionTranslator() ExceptionTranslator
  + configuration() DefaultConfiguration
  + dsl() DefaultDSLContext
}
class EmailChecker {
  + isValidEmail(String) boolean
}
class EmailErrorDto {
  ~ String code
  ~ String message
  + setCode(String) void
  + toString() String
  + equals(Object) boolean
  + getMessage() String
  + getCode() String
  + setMessage(String) void
  # canEqual(Object) boolean
  + hashCode() int
}
class EmailProperties {
  - String host
  - String protocol
  - String port
  + setPort(String) void
  + getHost() String
  + setHost(String) void
  + getPort() String
  + setProtocol(String) void
  + emailConfig() Properties
  + getProtocol() String
}
class EmailService {
<<Interface>>
  + sendMessage(String, String) void
}
class EmailServiceImpl {
  - SenderProperties sender
  - Properties emailConfig
  + sendMessage(String, String) void
}
class ErrorDto {
  ~ String code
  ~ String message
  + toString() String
  # canEqual(Object) boolean
  + getMessage() String
  + setMessage(String) void
  + hashCode() int
  + getCode() String
  + equals(Object) boolean
  + setCode(String) void
}
class ErrorList
class ErrorsMap
class ExceptionAdvice {
  - Logger log
  - ErrorList errorList
  + handleBadRequestException(RuntimeException) ResponseEntity~?~
  + handleUnauthorizedException(RuntimeException) ResponseEntity~?~
  + handleExternalServiceException(RuntimeException) ResponseEntity~?~
  - buildResponseWrappedDtoFromException(RuntimeException) ResponseWrappedDto
}
class ExceptionTranslator {
  - Logger log
  + exception(ExecuteContext) void
}
class JWTService {
<<Interface>>

}
class JWTServiceImpl
class Keys {
  + UniqueKey~UserRecord~ PK_USER_UUID
}
class MapperConfiguration {
  + modelMapper() ModelMapper
  - setUpMapper(ModelMapper) void
}
class MetaDto {
  - int size
  - List~String~ sort
  - int page
  - long total
  - String afterValue
  - long totalPages
  + getSort() List~String~
  + setTotalPages(long) void
  + getSize() int
  + getTotal() long
  + setSize(int) void
  + setTotal(long) void
  + getAfterValue() String
  # canEqual(Object) boolean
  + hashCode() int
  + getPage() int
  + setSort(List~String~) void
  + equals(Object) boolean
  + setPage(int) void
  + toString() String
  + setAfterValue(String) void
  + getTotalPages() long
}
class NoSuchEntityException
class Public {
  + User USER
  - long serialVersionUID
  + Public PUBLIC
  + AuthCode AUTH_CODE
  + getCatalog() Catalog?
  + getTables() List~Table~?~~
}
class RandomGenerator {
  - Random random
  + generateCode(int, int) String
}
class ResponseConverter {
  + responseWrappedFromErrorsDto(List~ErrorDto~) ResponseWrappedDto
  + convertToResponseWrappedDtoFromBindingResult(BindingResult) ResponseWrappedDto
  + convertToResponseWrappedDto(Object) ResponseWrappedDto
  + convertToResponseWrappedDtoFromErrorList(List~String~) ResponseWrappedDto
}
class ResponseWrappedDto {
  - Object response
  - boolean status
  - List~ErrorDto~ errors
  + toString() String
  + equals(Object) boolean
  + isStatus() boolean
  # canEqual(Object) boolean
  + setResponse(Object) void
  + setStatus(boolean) void
  + hashCode() int
  + getErrors() List~ErrorDto~
  + setErrors(List~ErrorDto~) void
  + getResponse() Object
}
class ResponseWrapperAdvice {
  - Logger log
  + supports(MethodParameter, Class) boolean
  + beforeBodyWrite(Object, MethodParameter, MediaType, Class, ServerHttpRequest, ServerHttpResponse) Object?
}
class SenderProperties {
  - String username
  - String email
  - String password
  + setUsername(String) void
  + setEmail(String) void
  + getPassword() String
  + getUsername() String
  + getEmail() String
  + setPassword(String) void
}
class Tables {
  + AuthCode AUTH_CODE
  + User USER
}
class TooMuchRequestsException
class UnauthorizedException
class User {
  - long serialVersionUID
  + TableField~UserRecord, UUID~ ID
  + TableField~UserRecord, String~ USERNAME
  + User USER
  + TableField~UserRecord, String~ EMAIL
  + TableField~UserRecord, Instant~ CREATION_DATE
  + as(Table~?~) User
  + mapping(Function4~UUID, String, String, Instant, U~) SelectField~U~
  + mapping(Class~U~, Function4~UUID, String, String, Instant, U~) SelectField~U~
  + rename(Name) User
  + getRecordType() Class~UserRecord~
  + as(String) User
  + fieldsRow() Row4~UUID, String, String, Instant~
  + getSchema() Schema?
  + getPrimaryKey() UniqueKey~UserRecord~?
  + as(Name) User
  + rename(Table~?~) User
  + rename(String) User
}
class UserCreateDto {
  - String username
  - String email
  + getEmail() String
  # canEqual(Object) boolean
  + hashCode() int
  + getUsername() String
  + equals(Object) boolean
  + toString() String
  + setEmail(String) void
  + setUsername(String) void
}
class UserDto {
  - String username
  - UUID id
  - String email
  + getId() UUID
  + toString() String
  + getEmail() String
  + getUsername() String
  + setId(UUID) void
  + setEmail(String) void
  + setUsername(String) void
  + equals(Object) boolean
  # canEqual(Object) boolean
  + hashCode() int
}
class UserEntity {
  - UUID id
  - String username
  - Instant creationDate
  - String email
  # canEqual(Object) boolean
  + setUsername(String) void
  + builder() UserEntityBuilder
  + toString() String
  + getId() UUID
  + equals(Object) boolean
  + getCreationDate() Instant
  + getEmail() String
  + hashCode() int
  + setEmail(String) void
  + setId(UUID) void
  + getUsername() String
  + setCreationDate(Instant) void
}
class UserRecord {
  - long serialVersionUID
  + field4() Field~Instant~
  + values(UUID, String, String, Instant) UserRecord
  + setCreationDate(Instant) void
  + component2() String
  + field3() Field~String~
  + getUsername() String
  + setId(UUID) void
  + setUsername(String) void
  + key() Record1~UUID~
  + value4() Instant
  + value4(Instant) UserRecord
  + setEmail(String) void
  + value3() String
  + valuesRow() Row4~UUID, String, String, Instant~
  + component1() UUID
  + value2() String
  + component3() String
  + getCreationDate() Instant
  + field1() Field~UUID~
  + component4() Instant
  + value1() UUID
  + value1(UUID) UserRecord
  + value3(String) UserRecord
  + value2(String) UserRecord
  + fieldsRow() Row4~UUID, String, String, Instant~
  + field2() Field~String~
  + getEmail() String
  + getId() UUID
}
class UserRepository {
<<Interface>>
  + insert(UserEntity) void
}
class UserRepositoryImpl {
  - Logger log
  - DSLContext dsl
  + insert(UserEntity) void
}
class UserService {
<<Interface>>
  + create(String) UUID
}
class UserServiceApplication {
  + main(String[]) void
}
class UserServiceImpl {
  - ModelMapper modelMapper
  - Logger log
  - UserRepository userRepository
  + create(String) UUID
}

AuthCodeRepositoryImpl  ..>  AuthCodeRepository 
AuthControllerImpl  ..>  AuthController 
AuthControllerImpl "1" *--> "authService 1" AuthService 
AuthServiceImpl "1" *--> "authCodeRepository 1" AuthCodeRepository 
AuthServiceImpl  ..>  AuthService 
AuthServiceImpl "1" *--> "emailService 1" EmailService 
CollectionResponseDto "1" *--> "meta 1" MetaDto 
DefaultCatalog "1" *--> "PUBLIC 1" Public 
EmailServiceImpl  ..>  EmailService 
ExceptionAdvice "1" *--> "errorList 1" ErrorList 
JWTServiceImpl  ..>  JWTService 
Public "1" *--> "AUTH_CODE 1" AuthCode 
Public "1" *--> "USER 1" User 
ResponseWrappedDto "1" *--> "errors *" ErrorDto 
Tables "1" *--> "AUTH_CODE 1" AuthCode 
Tables "1" *--> "USER 1" User 
UserRepositoryImpl  ..>  UserRepository 
UserServiceImpl "1" *--> "userRepository 1" UserRepository 
UserServiceImpl  ..>  UserService 
