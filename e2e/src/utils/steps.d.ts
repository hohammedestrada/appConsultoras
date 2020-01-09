
type ICodeceptCallback = (i: CodeceptJS.I) => void;

declare class FeatureConfig {
  retry(times:number): FeatureConfig
  timeout(seconds:number): FeatureConfig
  config(config:object): FeatureConfig
  config(helperName:string, config:object): FeatureConfig
}

declare class ScenarioConfig {
  throws(err:any) : ScenarioConfig;
  fails() : ScenarioConfig;
  retry(times:number): ScenarioConfig
  timeout(timeout:number): ScenarioConfig
  inject(inject:object): ScenarioConfig
  config(config:object): ScenarioConfig
  config(helperName:string, config:object): ScenarioConfig
}

interface ILocator {
  xpath?: string;
  css?: string;
  name?: string;
  value?: string;
  frame?: string;
  android?: string;
  ios?: string;
}

declare class Helper {
  /** Abstract method to provide required config options */
  static _config(): any;
  /** Abstract method to validate config */
  _validateConfig<T>(config: T): T;
  /** Sets config for current test */
  _setConfig(opts: any): void;
  /** Hook executed before all tests */
  _init(): void
  /** Hook executed before each test. */
  _before(): void
  /** Hook executed after each test */
  _after(): void
  /**
   * Hook provides a test details
   * Executed in the very beginning of a test
   */
  _test(test: () => void): void
  /** Hook executed after each passed test */
  _passed(test: () => void): void
  /** Hook executed after each failed test */
  _failed(test: () => void): void
  /** Hook executed before each step */
  _beforeStep(step: () => void): void
  /** Hook executed after each step */
  _afterStep(step: () => void): void
  /** Hook executed before each suite */
  _beforeSuite(suite: () => void): void
  /** Hook executed after each suite */
  _afterSuite(suite: () => void): void
  /** Hook executed after all tests are executed */
  _finishTest(suite: () => void): void
  /**Access another configured helper: this.helpers['AnotherHelper'] */
  readonly helpers: any
  /** Print debug message to console (outputs only in debug mode) */
  debug(msg: string): void

  debugSection(section: string, msg: string): void
}

declare class Locator implements ILocator {
  xpath?: string;
  css?: string;
  name?: string;
  value?: string;
  frame?: string;
  android?: string;
  ios?: string;

  or(locator:string): Locator;
  find(locator:string): Locator;
  withChild(locator:string): Locator;
  find(locator:string): Locator;
  at(position:number): Locator;
  first(): Locator;
  last(): Locator;
  inside(locator:string): Locator;
  before(locator:string): Locator;
  after(locator:string): Locator;
  withText(locator:string): Locator;
  withAttr(locator:object): Locator;
  as(locator:string): Locator;
}


declare function actor(customSteps?: {
  [action: string]: (this: CodeceptJS.I, ...args: any[]) => void
}): CodeceptJS.I;
declare function actor(customSteps?: {}): CodeceptJS.I;
declare function Feature(title: string, opts?: {}): FeatureConfig;
declare const Scenario: {
  (title: string, callback: ICodeceptCallback): ScenarioConfig;
  (title: string, opts: {}, callback: ICodeceptCallback): ScenarioConfig;
  only(title: string, callback: ICodeceptCallback): ScenarioConfig;
  only(title: string, opts: {}, callback: ICodeceptCallback): ScenarioConfig;
}
declare function xScenario(title: string, callback: ICodeceptCallback): ScenarioConfig;
declare function xScenario(title: string, opts: {}, callback: ICodeceptCallback): ScenarioConfig;
declare function Data(data: any): any;
declare function xData(data: any): any;
declare function Before(callback: ICodeceptCallback): void;
declare function BeforeSuite(callback: ICodeceptCallback): void;
declare function After(callback: ICodeceptCallback): void;
declare function AfterSuite(callback: ICodeceptCallback): void;

declare function locate(selector: string): Locator;
declare function locate(selector: ILocator): Locator;
declare function within(selector: string, callback: Function): Promise<any>;
declare function within(selector: ILocator, callback: Function): Promise<any>;
declare function session(selector: string, callback: Function): Promise<any>;
declare function session(selector: ILocator, callback: Function): Promise<any>;
declare function session(selector: string, config: any, callback: Function): Promise<any>;
declare function session(selector: ILocator, config: any, callback: Function): Promise<any>;
declare function pause(): void;

declare const codeceptjs: any;

declare namespace CodeceptJS {
  export interface I {
    runOnIOS(caps: string, fn: Function) : void,
    runOnAndroid(caps: string, fn: Function) : void,
    runInWeb(fn: Function) : void,
    seeAppIsInstalled(bundleId: string) : void,
    seeAppIsNotInstalled(bundleId: string) : void,
    installApp(path: string) : void,
    removeApp(bundleId: string) : void,
    seeCurrentActivityIs(currentActivity: string) : void,
    seeDeviceIsLocked() : void,
    seeDeviceIsUnlocked() : void,
    seeOrientationIs(orientation: string) : void,
    setOrientation(orientation: string) : void,
    grabAllContexts() : Promise<string>,
    grabContext() : Promise<string>,
    grabCurrentActivity() : Promise<string>,
    grabNetworkConnection() : Promise<string>,
    grabOrientation() : Promise<string>,
    grabSettings() : Promise<string>,
    switchToWeb(context: ILocator) : void,
    switchToWeb(context: string) : void,
    switchToNative(context?: ILocator) : void,
    switchToNative(context?: string) : void,
    startActivity(appPackage: string, appActivity: string) : void,
    setNetworkConnection(value: string) : void,
    setSettings(settings: string) : void,
    hideDeviceKeyboard(strategy: string, key: string) : void,
    sendDeviceKeyEvent(keyValue: string) : void,
    openNotifications() : void,
    makeTouchAction(locator: ILocator, action: string) : void,
    makeTouchAction(locator: string, action: string) : void,
    tap(locator: ILocator) : void,
    tap(locator: string) : void,
    swipe(locator: ILocator, xoffset: string, yoffset: string, speed?: string) : void,
    swipe(locator: string, xoffset: string, yoffset: string, speed?: string) : void,
    swipeDown(locator: ILocator, yoffset?: string, speed: string) : void,
    swipeDown(locator: string, yoffset?: string, speed: string) : void,
    swipeLeft(locator: ILocator, xoffset?: string, speed: string) : void,
    swipeLeft(locator: string, xoffset?: string, speed: string) : void,
    swipeRight(locator: ILocator, xoffset?: string, speed: string) : void,
    swipeRight(locator: string, xoffset?: string, speed: string) : void,
    swipeUp(locator: ILocator, yoffset?: string, speed: string) : void,
    swipeUp(locator: string, yoffset?: string, speed: string) : void,
    swipeTo(searchableLocator: string, scrollLocator: string, direction: string, timeout: string, offset: number, speed: string) : void,
    touchPerform(actions: string) : void,
    pullFile(path: string, dest: string) : void,
    shakeDevice() : void,
    rotate(x: string, y: string, duration: string, radius: string, rotation: string, touchCount: string) : void,
    setImmediateValue(id: string, value: string) : void,
    simulateTouchId(match: string) : void,
    closeApp() : void,
    appendField() : void,
    checkOption() : void,
    click() : void,
    dontSeeCheckboxIsChecked() : void,
    dontSeeElement() : void,
    dontSeeInField() : void,
    dontSee() : void,
    fillField() : void,
    grabTextFrom() : Promise<string>,
    grabValueFrom() : Promise<string>,
    seeCheckboxIsChecked() : void,
    seeElement() : void,
    seeInField() : void,
    see() : void,
    selectOption() : void,
    waitForElement() : void,
    waitForVisible() : void,
    waitForInvisible() : void,
    waitForText() : void,
    defineTimeout(timeouts: string) : void,
    amOnPage(url: string) : void,
    doubleClick(locator: ILocator, context?: ILocator) : void,
    doubleClick(locator: string, context?: ILocator) : void,
    doubleClick(locator: ILocator, context?: string) : void,
    doubleClick(locator: string, context?: string) : void,
    rightClick(locator: ILocator, context: ILocator) : void,
    rightClick(locator: string, context: ILocator) : void,
    rightClick(locator: ILocator, context: string) : void,
    rightClick(locator: string, context: string) : void,
    clearField(field: ILocator) : void,
    clearField(field: string) : void,
    attachFile(locator: ILocator, pathToFile: string) : void,
    attachFile(locator: string, pathToFile: string) : void,
    uncheckOption(field: ILocator, context?: ILocator) : void,
    uncheckOption(field: string, context?: ILocator) : void,
    uncheckOption(field: ILocator, context?: string) : void,
    uncheckOption(field: string, context?: string) : void,
    grabHTMLFrom(locator: ILocator) : Promise<string>,
    grabHTMLFrom(locator: string) : Promise<string>,
    grabCssPropertyFrom(locator: ILocator, cssProperty: string) : Promise<string>,
    grabCssPropertyFrom(locator: string, cssProperty: string) : Promise<string>,
    grabAttributeFrom(locator: ILocator, attr: string) : Promise<string>,
    grabAttributeFrom(locator: string, attr: string) : Promise<string>,
    seeInTitle(text: string) : void,
    seeTitleEquals(text: string) : void,
    dontSeeInTitle(text: string) : void,
    grabTitle() : Promise<string>,
    seeTextEquals(text: string, context?: ILocator) : void,
    seeTextEquals(text: string, context?: string) : void,
    seeElementInDOM(locator: ILocator) : void,
    seeElementInDOM(locator: string) : void,
    dontSeeElementInDOM(locator: ILocator) : void,
    dontSeeElementInDOM(locator: string) : void,
    seeInSource(text: string) : void,
    grabSource() : Promise<string>,
    grabBrowserLogs() : Promise<string>,
    grabCurrentUrl() : Promise<string>,
    dontSeeInSource(text: string) : void,
    seeNumberOfElements(locator: ILocator, num: number) : void,
    seeNumberOfElements(locator: string, num: number) : void,
    seeNumberOfVisibleElements(locator: ILocator, num: number) : void,
    seeNumberOfVisibleElements(locator: string, num: number) : void,
    seeCssPropertiesOnElements(locator: ILocator, cssProperties: string) : void,
    seeCssPropertiesOnElements(locator: string, cssProperties: string) : void,
    seeAttributesOnElements(locator: ILocator, attributes: string) : void,
    seeAttributesOnElements(locator: string, attributes: string) : void,
    grabNumberOfVisibleElements(locator: ILocator) : Promise<string>,
    grabNumberOfVisibleElements(locator: string) : Promise<string>,
    seeInCurrentUrl(url: string) : void,
    dontSeeInCurrentUrl(url: string) : void,
    seeCurrentUrlEquals(url: string) : void,
    dontSeeCurrentUrlEquals(url: string) : void,
    executeScript(fn: Function) : void,
    executeAsyncScript(fn: Function) : void,
    scrollTo(locator: ILocator, offsetX?: number, offsetY?: number) : void,
    scrollTo(locator: string, offsetX?: number, offsetY?: number) : void,
    moveCursorTo(locator: ILocator, offsetX?: number, offsetY?: number) : void,
    moveCursorTo(locator: string, offsetX?: number, offsetY?: number) : void,
    saveScreenshot(fileName: string, fullPage?: string) : void,
    setCookie(cookie: string) : void,
    clearCookie(cookie: string) : void,
    seeCookie(name: string) : void,
    dontSeeCookie(name: string) : void,
    grabCookie(name: string) : Promise<string>,
    acceptPopup() : void,
    cancelPopup() : void,
    seeInPopup(text: string) : void,
    grabPopupText() : Promise<string>,
    pressKey(key: string) : void,
    resizeWindow(width: number, height: number) : void,
    dragAndDrop(srcElement: string, destElement: string) : void,
    dragSlider(locator: ILocator, offsetX?: number) : void,
    dragSlider(locator: string, offsetX?: number) : void,
    closeOtherTabs() : void,
    wait(sec: number) : void,
    waitForEnabled(locator: ILocator, sec?: number) : void,
    waitForEnabled(locator: string, sec?: number) : void,
    waitUntilExists(locator: ILocator, sec?: number) : void,
    waitUntilExists(locator: string, sec?: number) : void,
    waitInUrl(urlPart: string, sec?: number) : void,
    waitUrlEquals(urlPart: string, sec?: number) : void,
    waitForValue(field: ILocator, value: string, sec?: number) : void,
    waitForValue(field: string, value: string, sec?: number) : void,
    waitNumberOfVisibleElements(locator: ILocator, num: number, sec?: number) : void,
    waitNumberOfVisibleElements(locator: string, num: number, sec?: number) : void,
    waitToHide(locator: ILocator, sec?: number) : void,
    waitToHide(locator: string, sec?: number) : void,
    waitForStalenessOf(locator: ILocator, sec?: number) : void,
    waitForStalenessOf(locator: string, sec?: number) : void,
    waitForDetached(locator: ILocator, sec?: number) : void,
    waitForDetached(locator: string, sec?: number) : void,
    waitForFunction(fn: Function, argsOrSec?: string, sec?: number) : void,
    waitUntil(fn: Function, sec?: number, timeoutMsg?: string, interval?: string) : void,
    switchTo(locator: ILocator) : void,
    switchTo(locator: string) : void,
    switchToNextTab(num?: number, sec?: number) : void,
    switchToPreviousTab(num?: number, sec?: number) : void,
    closeCurrentTab() : void,
    openNewTab() : void,
    grabNumberOfOpenTabs() : Promise<string>,
    refreshPage() : void,
    scrollPageToTop() : void,
    scrollPageToBottom() : void,
    grabPageScrollPosition() : Promise<string>,
    debug(msg: string) : void,
    debugSection(section: string, msg: string) : void,
    addMochawesomeContext(context: ILocator) : void,
    addMochawesomeContext(context: string) : void,
    say: () => any; 
    retryStep(opts: string) : void,

  }

}

declare module "codeceptjs" {
    export = CodeceptJS;
}
