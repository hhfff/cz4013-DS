def a(x):
    if x == 0:
        raise Exception('Error: I am python')
    print("heheheeh")


try:
    print("start")
    a(0)

except Exception as e:
    print("error")
    pass
print("end")