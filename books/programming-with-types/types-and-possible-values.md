# Types and possible values

| Name                  | TypeScript type                 | Possible values                                             |
| --------------------- |:------------------------------- | -----------------------------------------------------------:|
| Empty type            | never                           | No possible values                                          |
| Unit type             | void                            | One possible value |
| Sum type              | number | string                 | A value from number or a value from string |
| Tuple (product type)  | [number, string]                | A value from number and a value from string |
| Record (product type) | { a: number, b: string }        | A (named) value from number and a (named) value from string |
| Function type         | (value: number) => string       | A function number -> string |
| Top type              | unknown                         | A value of any type |
| Bottom type           | never               | No possible values ( the bottom type is the subtype of any other type ) |
| Interface             | interface ILogger { /* ...*/  } | Object of a type that implements the ILogger interface |
| Class                 | class Square { /** ...*/ }      | Object of type Square |
| Intersection type     | Square & Loggable               | Object with members of both Square and Loggable |
| Generic class         | class List<T> { /* ...*/ }      | A generic class List with a type parameter T |
| Generic function      | type Func<T, U> = (arg: T) => U | A function from T -> U where T and U are the type parameters |
