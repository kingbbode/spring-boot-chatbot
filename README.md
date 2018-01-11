# Kingbbode Spring Boot Chatbot (COMING SOON....)

- spring-boot-chatbot-line-starter
- spring-boot-chatbot-teamup-starter

## echo example

```java
@Brain
public class FirstBrain {

    @BrainCell(key = "따라해봐", function = "echo-start")
    public String echo(BrainRequest brainRequest) {
        return  "말해봐";
    }

    @BrainCell(key = "따라해봐", function = "echo-end", parent = "echo-start")
    public String echo2(BrainRequest brainRequest) {
        return  brainRequest.getContent();
    }
}
```