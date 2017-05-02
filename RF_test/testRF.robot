*** Settings ***
Library           Collections
Library           Selenium2Library
Library           RequestsLibrary
Library           RadLibrary
Resource          keyword.txt

*** Variables ***
${hi}             欢迎欢迎
@{student}        小明    小红    小花

*** Test Cases ***
case1
    ${ji}    Catenate    hello    world
    log    ${ji}
    ${ab}    Create List    a    b
    log    ${ab}
    @{abc}    Create List    a    b    c
    log many    @{abc}
    ${t}    get time    hee
    sleep    5
    ${t}    get time    hee
    ${a}    Set variable    56
    log    ${a}>=90
    run keyword if    ${a}>=90    log    优秀
    ...    ELSE IF    ${a}>=70    log    良好
    ...    ELSE IF    ${a}<70    log    及格
    : FOR    ${i}    IN RANGE    10
    \    log    ${i}
    @{abc}    create list    a    b    c
    : FOR    ${i}    IN    @{abc}
    \    log    ${i}
    @{abc}    create list    a    b    c
    : FOR    ${i}    IN    @{abc}
    \    Exit For Loop If    '${i}'=='c'
    \    log    ${i}

Evalute
    ${d}    Evaluate    random.randint(1000,9999)    random
    log    ${d}
    Comment    Evaluate    os.system('python C:/Users/admin/PycharmProjects/untitled/charpter14.py')    f
    Import Library    C:/Users/admin/PycharmProjects/untitled/charpter14.py
    @{ss}    f
    log many    @{ss}
    Take Screenshot

Dic
    ${dict}    Create Dictionary    a    1    b    2
    ${items}    Get Dictionary Items    ${dict}
    log    ${items}
    ${key}    Get Dictionary Keys    ${dict}
    log    ${key}
    ${value}    Get Dictionary Values    ${dict}
    log    ${value}
    ${v}    Get From Dictionary    ${dict}    b
    log    ${v}

Edit
    [Documentation]    测试用例 http://www.baidu.com
    [Tags]    重要
    log    王导${hi}
    Comment    : FOR    ${n}    IN    @{student}
    Comment    \    log    ${n}
    循环    8

test
    log    ${hi}
    log many    @{student}
    ${shuzii}    Set Variable    ${9.11}    9.11
    ${val}    Set Variable    abcd
    ${val2}    Set Variable If    '${val}'=='abcd'    ksjkwnfkjqwkfj    ssss
    ${getVal1}    Get Length    ${val}
    ${getVal2}    Get Time
    log    ${getVal1}
    Run Keyword If    '${val}'=='abcd'    log    .......
    log    121233${val2}sdf
    log    12313+aaaa
    log    ${val2[2]}
    log    ${val2[0:3]}
    ${cal1}    Set Variable    123
    ${cal2}    Evaluate    ${cal1}+2
    ${cal3}    Evaluate    int(${cal1})+2
    @{val3}    Set Variable    1    2    3
    @{listVal3}    Create List    3    2    1
    Run Keyword    log    abcd    WARN
    @{argVal3}    Create List    zz...sfd    WARN    55    987
    @{argVal4}    Create List    a    WARN
    Comment    ${keyword}    Set Variable    log
    Comment    Run Keyword    ${keyword}    @{argVal3}
    log    ${argVal3}

List
    @{listA}    Create List    a    b
    @{listB}    Create List    1    2
    @{listC}    Create List    ${listA}    ${listB}
    @{listD}    Create List    @{listA}    @{listB}
    log    @{listC}[1]
    log    @{listC[1]}[1]
    @{argVal5}    Create List    abcd    WARN
    log    ${argVal5}
    @{argVal4}    Create List    a    WARN
    log    @{argVal4}
    @{argVal3}    Create List    a    WARN
    log    ${argVal3}
    ${argVal6}    Create List    a    WARN
    log    dd
    ${argVal8}    Create List    a    b
    log    ==@{argVal8}==
    @{argVal8}    Create List    777    999
    log    ${argVal8}

Keyword
    随机字符    aaa    222
    Comment    随机字符List-1    23
    ${getArg1}    随机字符List-1    abc    aa    ss    ff
    log    ${getArg1}
    @{getArg2}    随机字符List-2    abc    aa    111    222
    log    =@{getArg2}=
    ${a}    ${b}    随机字符List-2    abc    aa    111
    log    ${a}=${b}

reg
    ${time}    Stime
    ${number}    createnumber
    ${url}    Set Variable    http://192.168.10.125:8080/
    Open Browser    ${url}    chrome
    sleep    2
    Clear Element Text    xpath=.//*[@id='wndMainLogin']/div/form/table/tbody/tr[1]/td[2]/span/input[1]
    sleep    5
    input text by path    .//*[@id='wndMainLogin']/div/form/table/tbody/tr[1]/td[2]/span/input[1]    admin
    input text by id    txtMainLoginPwd    12345
    click element    xpath=.//*[@id='btnMainLogin']/span/span
    sleep    3
    Comment    close browser

*** Keywords ***
循环
    [Arguments]    ${number}
    : FOR    ${i}    IN RANGE    ${number}
    \    log    ${i}
