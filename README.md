# Kingbbode Spring Boot Chatbot (COMING SOON....)

- chatbot-spring-boot-line-starter
- chatbot-spring-boot-teamup-starter
- chatbot-spring-boot-telegram-starter

## echo example

```java
@Brain
public class FirstBrain {

    @BrainCell(key = "따라해봐", function = "echo-start")
    public String echo(BrainRequest brainRequest) {
        return  "말해봐";
    }

    @BrainCell(function = "echo-end", parent = "echo-start")
    public String echo2(BrainRequest brainRequest) {
        return  brainRequest.getContent();
    }
}
```