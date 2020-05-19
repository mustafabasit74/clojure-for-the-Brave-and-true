;; (.methodName object)

(.toUpperCase "Java Interop")
;; => "JAVA INTEROP"

(.indexOf "Java Interop" "e")
;; => 8
;; "Java Interop".indexOf("e") - java Equivalent

(java.lang.Math/abs -5)
;; => 5

(Math/abs -5)
;; => 5

java.lang.Math/PI
;; => 3.141592653589793

(macroexpand '(.toUpperCase "Java Interop"))
;; => (. "Java Interop" toUpperCase)

(. "Java Interop" toUpperCase)
;; => "JAVA INTEROP"

(macroexpand '(.indexOf "Java Interop" "e"))
;; => (. "Java Interop" indexOf "e")

(. "Java Interop" indexOf "e")
;; => 8

(macroexpand '(java.lang.Math/abs -5))
;; => (. java.lang.Math abs -5)

(java.lang.Math/PI)
;; => 3.141592653589793

(macroexpand '(java.lang.Math/PI))
;; => (. java.lang.Math PI)

;; general form of the dot operator:
;; (. object-expr-or-classname-symbol method-or-member-symbol optional-args*)




;; Creating and Mutating Objects
;; (new ClassName optional-args*) (ClassName. optional-args*):

(new String)
;; => ""

(String.)
;; => ""

(new String "Java Interop")
;; => "Java Interop"

(String. "Java Interop")
;; => "Java Interop"

(new java.util.Stack)
;; => []

(java.util.Stack.)
;; => []

(let [stack (java.util.Stack.)]
  (.push stack "Latest episode of Game of Thrones, ho!")
  stack)
;; => ["Latest episode of Game of Thrones, ho!"]

(let [stack (java.util.Stack.)]
  (.push stack "Latest episode of Game of Thrones, ho!")
  (.push stack "foo")
  (.pop stack)
  stack)
;; => ["Latest episode of Game of Thrones, ho!"]


(let [stack (java.util.Stack.)]
  (.push stack "Latest episode of Game of Thrones, ho!")
  (.push stack "foo")
  (.push stack "Hello World")
  stack)
;; => ["Latest episode of Game of Thrones, ho!" "foo" "Hello World"]


(let [stack (java.util.Stack.)]
  (.push stack "Latest episode of Game of Thrones, ho!")
  (.push stack "foo")
  (last stack))
;; => "foo"

(let [stack (java.util.Stack.)]
  (.push stack "Latest episode of Game of Thrones, ho!")
  (conj stack 1))
;; => Execution error (ClassCastException) at user/eval21112 (form-init8892847606662886496.clj:102).
;;    class java.util.Stack cannot be cast to class clojure.lang.IPersistentCollection (java.util.Stack is in module java.base of loader 'bootstrap'; clojure.lang.IPersistentCollection is in unnamed module of loader 'app')


;; Clojure provides the doto macro, which allows you to execute multiple
;; methods on the same object more succinctly
(doto (java.util.Stack.)
  (.push "Latest episode of Game of Thrones, ho!")
  (.push "Whoops, I meant 'Land, ho!'"))
;; => ["Latest episode of Game of Thrones, ho!" "Whoops, I meant 'Land, ho!'"]

(macroexpand-1 '(doto (java.util.Stack.)
                  (.push "Latest episode of Game of Thrones, ho!")
                  (.push "Whoops, I meant 'Land, ho!'")))
;; => (clojure.core/let
;;     [G__23879 (java.util.Stack.)]
;;     (.push G__23879 "Latest episode of Game of Thrones, ho!")
;;     (.push G__23879 "Whoops, I meant 'Land, ho!'")
;;     G__23879)




;; Importing
(import java.util.Stack)
(new Stack)
;; => []


(import [java.util Date Stack]
        [java.net Proxy URI])

(Date.)
;; => #inst "2020-05-19T09:03:02.004-00:00"


(let [date (Date.)]
  (println "Day:" (.getDay date))
  (println "Month:" (.getMonth date)))
;; => Day: 2
;;    Month: 4



;; Commonly Used Java Classes
(System/exit 1)

;; computer's environment variables
(System/getenv)
;; => {"PATH" "/home/mustafa-basit/.local/bin:/home/mustafa-basit/.local/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin", "INVOCATION_ID" "ff5b059394524d47a10ee407fb30f070", "XAUTHORITY" "/run/user/1000/gdm/Xauthority", "XMODIFIERS" "@im=ibus", "XDG_DATA_DIRS" "/usr/share/ubuntu:/usr/local/share/:/usr/share/:/var/lib/snapd/desktop", "GDMSESSION" "ubuntu", "VSCODE_LOG_STACK" "false", "GTK_IM_MODULE" "ibus", "DBUS_SESSION_BUS_ADDRESS" "unix:path=/run/user/1000/bus", "VSCODE_PID" "802", "XDG_CURRENT_DESKTOP" "Unity", "JOURNAL_STREAM" "9:1707753", "SSH_AGENT_PID" "1539", "COLORTERM" "truecolor", "QT4_IM_MODULE" "ibus", "SESSION_MANAGER" "local/mustafabasit:@/tmp/.ICE-unix/1635,unix/mustafabasit:/tmp/.ICE-unix/1635", "USERNAME" "mustafa-basit", "LOGNAME" "mustafa-basit", "APPLICATION_INSIGHTS_NO_DIAGNOSTIC_CHANNEL" "true", "TERM_PROGRAM_VERSION" "1.44.2", "PWD" "/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true", "MANAGERPID" "1368", "IM_CONFIG_PHASE" "1", "VSCODE_NLS_CONFIG" "{\"locale\":\"en-gb\",\"availableLanguages\":{},\"_languagePackSupport\":true}", "AMD_ENTRYPOINT" "vs/workbench/services/extensions/node/extensionHostProcess", "LANGUAGE" "en_IN:en", "SHELL" "/bin/bash", "LESSOPEN" "| /usr/bin/lesspipe %s", "CHROME_DESKTOP" "code-url-handler.desktop", "LEIN_JVM_OPTS" "-Xbootclasspath/a:/usr/share/java/leiningen-2.9.0.jar -Xverify:none -XX:+TieredCompilation -XX:TieredStopAtLevel=1", "GIO_LAUNCHED_DESKTOP_FILE" "/usr/share/applications/code.desktop", "LEIN_VERSION" "2.9.0", "JVM_OPTS" "", "GNOME_DESKTOP_SESSION_ID" "this-is-deprecated", "GTK_MODULES" "gail:atk-bridge", "VSCODE_NODE_CACHED_DATA_DIR" "/home/mustafa-basit/.config/Code/CachedData/ff915844119ce9485abfe8aa9076ec76b5300ddd", "CLUTTER_IM_MODULE" "ibus", "VERBOSE_LOGGING" "true", "XDG_SESSION_DESKTOP" "ubuntu", "LS_COLORS" "", "SHLVL" "1", "LEIN_HOME" "/home/mustafa-basit/.lein", "LESSCLOSE" "/usr/bin/lesspipe %s %s", "VSCODE_HANDLES_UNCAUGHT_ERRORS" "true", "QT_IM_MODULE" "ibus", "TERM" "xterm-256color", "XDG_CONFIG_DIRS" "/etc/xdg/xdg-ubuntu:/etc/xdg", "LANG" "en_GB.UTF-8", "XDG_SESSION_TYPE" "x11", "PIPE_LOGGING" "true", "DISPLAY" ":0", "ELECTRON_RUN_AS_NODE" "1", "XDG_SESSION_CLASS" "user", "_" "/usr/bin/java", "VSCODE_IPC_HOOK_EXTHOST" "/tmp/vscode-ipc-09745e7b-ff03-4a3b-becd-dfa913398bb1.sock", "TERM_PROGRAM" "vscode", "NOTIFY_SOCKET" "/run/user/1000/systemd/notify", "GPG_AGENT_INFO" "/run/user/1000/gnupg/S.gpg-agent:0:1", "DESKTOP_SESSION" "ubuntu", "VSCODE_LOGS" "/home/mustafa-basit/.config/Code/logs/20200518T180045", "USER" "mustafa-basit", "XDG_MENU_PREFIX" "gnome-", "GIO_LAUNCHED_DESKTOP_FILE_PID" "802", "LEIN_JAVA_CMD" "java", "WINDOWPATH" "2", "JAVA_CMD" "java", "SSH_AUTH_SOCK" "/run/user/1000/keyring/ssh", "GSETTINGS_SCHEMA_DIR" "/home/mustafa-basit/data", "GNOME_SHELL_SESSION_MODE" "ubuntu", "NO_AT_BRIDGE" "1", "TRAMPOLINE_FILE" "/tmp/lein-trampoline-2xHOcwtq9K3wI", "XDG_RUNTIME_DIR" "/run/user/1000", "HOME" "/home/mustafa-basit", "VSCODE_IPC_HOOK" "/run/user/1000/vscode-4062c316-1.44.2-main.sock"}

;; JVM properties
(System/getProperties)
;; => {"sun.desktop" "gnome", "awt.toolkit" "sun.awt.X11.XToolkit", "clojure-for-the-brave-and-true.version" "0.1.0-SNAPSHOT", "java.specification.version" "11", "sun.cpu.isalist" "", "sun.jnu.encoding" "UTF-8", "clojure.debug" "false", "java.class.path" "/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true/test:/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true/src:/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true/dev-resources:/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true/resources:/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true/target/default/classes:/home/mustafa-basit/.m2/repository/clojure-complete/clojure-complete/0.2.5/clojure-complete-0.2.5.jar:/home/mustafa-basit/.m2/repository/org/msgpack/msgpack/0.6.12/msgpack-0.6.12.jar:/home/mustafa-basit/.m2/repository/org/clojure/tools.reader/1.3.2/tools.reader-1.3.2.jar:/home/mustafa-basit/.m2/repository/tigris/tigris/0.1.1/tigris-0.1.1.jar:/home/mustafa-basit/.m2/repository/com/cognitect/transit-clj/0.8.313/transit-clj-0.8.313.jar:/home/mustafa-basit/.m2/repository/org/clojure/clojure/1.10.0/clojure-1.10.0.jar:/home/mustafa-basit/.m2/repository/javax/xml/bind/jaxb-api/2.3.0/jaxb-api-2.3.0.jar:/home/mustafa-basit/.m2/repository/org/clojure/core.async/1.2.603/core.async-1.2.603.jar:/home/mustafa-basit/.m2/repository/nrepl/nrepl/0.6.0/nrepl-0.6.0.jar:/home/mustafa-basit/.m2/repository/org/ow2/asm/asm/5.2/asm-5.2.jar:/home/mustafa-basit/.m2/repository/org/clojure/core.cache/0.8.2/core.cache-0.8.2.jar:/home/mustafa-basit/.m2/repository/org/clojure/tools.analyzer/1.0.0/tools.analyzer-1.0.0.jar:/home/mustafa-basit/.m2/repository/cider/cider-nrepl/0.23.0/cider-nrepl-0.23.0.jar:/home/mustafa-basit/.m2/repository/org/clojure/core.memoize/0.8.2/core.memoize-0.8.2.jar:/home/mustafa-basit/.m2/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar:/home/mustafa-basit/.m2/repository/clj-kondo/clj-kondo/2020.04.05/clj-kondo-2020.04.05.jar:/home/mustafa-basit/.m2/repository/org/clojure/tools.analyzer.jvm/1.0.0/tools.analyzer.jvm-1.0.0.jar:/home/mustafa-basit/.m2/repository/org/clojure/data.priority-map/0.0.7/data.priority-map-0.0.7.jar:/home/mustafa-basit/.m2/repository/com/cognitect/transit-java/0.8.337/transit-java-0.8.337.jar:/home/mustafa-basit/.m2/repository/cheshire/cheshire/5.8.1/cheshire-5.8.1.jar:/home/mustafa-basit/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.9.6/jackson-core-2.9.6.jar:/home/mustafa-basit/.m2/repository/com/fasterxml/jackson/dataformat/jackson-dataformat-cbor/2.9.6/jackson-dataformat-cbor-2.9.6.jar:/home/mustafa-basit/.m2/repository/org/clojure/core.specs.alpha/0.2.44/core.specs.alpha-0.2.44.jar:/home/mustafa-basit/.m2/repository/io/lambdaforge/datalog-parser/0.1.1/datalog-parser-0.1.1.jar:/home/mustafa-basit/.m2/repository/org/javassist/javassist/3.18.1-GA/javassist-3.18.1-GA.jar:/home/mustafa-basit/.m2/repository/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:/home/mustafa-basit/.m2/repository/org/clojure/spec.alpha/0.2.176/spec.alpha-0.2.176.jar:/home/mustafa-basit/.m2/repository/com/fasterxml/jackson/dataformat/jackson-dataformat-smile/2.9.6/jackson-dataformat-smile-2.9.6.jar", "java.vm.vendor" "Ubuntu", "sun.arch.data.model" "64", "java.vendor.url" "https://ubuntu.com/", "user.timezone" "Asia/Kolkata", "os.name" "Linux", "java.vm.specification.version" "11", "sun.java.launcher" "SUN_STANDARD", "user.country" "GB", "sun.boot.library.path" "/usr/lib/jvm/java-11-openjdk-amd64/lib", "sun.java.command" "clojure.main -i /tmp/form-init6443539833653205836.clj", "jdk.debug" "release", "sun.cpu.endian" "little", "user.home" "/home/mustafa-basit", "user.language" "en", "java.specification.vendor" "Oracle Corporation", "java.version.date" "2020-01-14", "java.home" "/usr/lib/jvm/java-11-openjdk-amd64", "file.separator" "/", "java.vm.compressedOopsMode" "32-bit", "line.separator" "\n", "java.specification.name" "Java Platform API Specification", "java.vm.specification.vendor" "Oracle Corporation", "java.awt.graphicsenv" "sun.awt.X11GraphicsEnvironment", "sun.management.compiler" "HotSpot 64-Bit Tiered Compilers", "java.runtime.version" "11.0.6+10-post-Ubuntu-1ubuntu119.10.1", "user.name" "mustafa-basit", "path.separator" ":", "os.version" "5.3.0-29-generic", "java.runtime.name" "OpenJDK Runtime Environment", "file.encoding" "UTF-8", "clojure.compile.path" "/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true/target/default/classes", "java.vm.name" "OpenJDK 64-Bit Server VM", "java.vendor.url.bug" "https://bugs.launchpad.net/ubuntu/+source/openjdk-lts", "java.io.tmpdir" "/tmp", "java.version" "11.0.6", "user.dir" "/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true", "os.arch" "amd64", "java.vm.specification.name" "Java Virtual Machine Specification", "java.awt.printerjob" "sun.print.PSPrinterJob", "sun.os.patch.level" "unknown", "java.library.path" "/usr/java/packages/lib:/usr/lib/x86_64-linux-gnu/jni:/lib/x86_64-linux-gnu:/usr/lib/x86_64-linux-gnu:/usr/lib/jni:/lib:/usr/lib", "java.vendor" "Ubuntu", "java.vm.info" "mixed mode", "java.vm.version" "11.0.6+10-post-Ubuntu-1ubuntu119.10.1", "sun.io.unicode.encoding" "UnicodeLittle", "apple.awt.UIElement" "true", "java.class.version" "55.0"}


(get (System/getenv) "USER")
;; => "mustafa-basit"

(System/getenv "USER")
;; => "mustafa-basit"

(System/getProperty "user.dir")
;; => "/home/mustafa-basit/Documents/Git@MustafaBasit/Clojure/clojure-for-the-brave-and-true"
;; returned the directory that the JVM started from

(System/getProperty "java.version")
;; => "11.0.6"




;; The Date Class
 check out - https://github.com/clj-time/clj-time




;; Files and Input/Output
(let [file (java.io.File. "/")]
  (println (.exists file))
  (println (.canWrite file))
  (println (.getPath file)))

(spit "/tmp/hercules-todo-list"
      "- Kill that lion brov
       - chop up what nasty multi-headed snake thing")

(slurp "/tmp/hercules-todo-list")
;; => "- Kill that lion brov\n       - chop up what nasty multi-headed snake thing"

 (let [s (java.io.StringWriter.)]
   (spit s "- capture cerynian hind like for real")
   (.toString s))
 ;; => "- capture cerynian hind like for real"

(let [s (java.io.StringReader. "- capture cerynian hind like for real")]
  (slurp s))
;; => "- capture cerynian hind like for real"

(with-open[todo-list-rdr (clojure.java.io/reader "/tmp/hercules-todo-list")]
  (println (first (line-seq todo-list-rdr))))
;; => - Kill that lion brov


;; “The Java Virtual Machine and Compilers Explained”
;; https://www.youtube.com/watch?v=XjNwyXx2os8
